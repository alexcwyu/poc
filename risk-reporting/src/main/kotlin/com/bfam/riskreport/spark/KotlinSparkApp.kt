package com.bfam.riskreport.spark

import com.bfam.riskreport.model.Value
import com.bfam.riskreport.service.FieldSchema
import com.bfam.riskreport.service.ReportRequest
import com.bfam.riskreport.service.ReportResponse
import com.bfam.riskreport.service.Schema
import com.google.protobuf.ByteString
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.RowFactory
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.substring_index
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.types.StructType


val path = "/Users/alex/dev/workspaces/poc/risk-reporting/src/main/resources/"

interface RiskManager {
    fun start()
    fun refresh()
    fun query(query: ReportRequest?): ReportResponse?
    fun stop()
}

class RiskMangerImpl() : RiskManager {
    val spark: SparkSession

    private var bigjoin: Dataset<Row>? = null

    init {
        this.spark = SparkSession
                .builder()
                .master("local")
                .appName("SparkRiskApp")
                .getOrCreate()
    }

    override fun refresh() {

        val riskDF = loadCSV(spark, path + "dashboard/risk.csv")
        val folioDF = loadCSV(spark, path + "dashboard/folio.csv")
        val instDF = loadCSV(spark, path + "dashboard/instrument.csv")
        val currencyDF = loadCSV(spark, path + "dashboard/currency.csv")
        val businessDF = createBusinessDF(spark)

        val enrichedCurrencyDF = currencyDF
                .withColumnRenamed("LIBELLE", "Currency LIBELLE")

        val enrichedFlioDF = folioDF
                .withColumnRenamed("CURRENCY", "Folio CURRENCY")
                .withColumnRenamed("SICOVAM", "Folio SICOVAM")
                .withColumnRenamed("TYPE", "Folio TYPE")

        val enrichedInstDF = instDF
                .withColumnRenamed("CURRENCY", "Instrument CURRENCY")
                .withColumnRenamed("LIBELLE", "Instrument LIBELLE")
                .withColumnRenamed("TYPE", "Instrument Type Code")
                .withColumnRenamed("SICOVAM", "Instrument SICOVAM")
                .withColumnRenamed("NOMINAL", "Instrument NOMINAL")

        val enrichedRiskDF = riskDF
                .withColumnRenamed("CURRENCY", "Position CURRENCY")
                .withColumnRenamed("NOMINAL", "Position NOMINAL")
                .withColumn("Folio", substring_index(col("Folio Path (SLOW!!)"), "/", -1))


        this.bigjoin = enrichedRiskDF
                .join(enrichedInstDF, enrichedRiskDF.col("Instrument code").equalTo(enrichedInstDF.col("Instrument SICOVAM")), "left_outer")
                .join(enrichedCurrencyDF, enrichedRiskDF.col("Position CURRENCY").equalTo(enrichedCurrencyDF.col("DEVISE")), "left_outer")
                .join(enrichedFlioDF, enrichedRiskDF.col("Folio").equalTo(enrichedFlioDF.col("IDENT")), "left_outer")
                .join(businessDF, enrichedRiskDF.col("SMOBook").equalTo(businessDF.col("SMOBookID")), "left_outer")


        bigjoin?.createOrReplaceGlobalTempView("bigjoin")
    }

    override fun query(query: ReportRequest?): ReportResponse? {
        val fields = "*"
        val df = spark.newSession().sql("select ${fields} from global_temp.bigjoin")
        return convert(df)
    }

    private fun convert(df:Dataset<Row>) : ReportResponse?{
        val responseBuilder = ReportResponse.newBuilder()
        responseBuilder.setSchema(convert(df.schema()))
        df.collectAsList().forEach { row ->
            val rowBuilder = com.bfam.riskreport.model.Row.newBuilder()
            row.schema().fields().forEach {
                field ->
                val valueBuilder = when(field.dataType()){
                    DataTypes.BooleanType  -> Value.newBuilder().setBoolValue(row.getAs(field.name())?:false)
                    DataTypes.IntegerType  -> Value.newBuilder().setIntValue(row.getAs(field.name())?:0)
                    DataTypes.LongType  -> Value.newBuilder().setLongValue(row.getAs(field.name())?:0)
                    DataTypes.FloatType  -> Value.newBuilder().setFloatValue(row.getAs(field.name())?:0.0f)
                    DataTypes.DoubleType  -> Value.newBuilder().setDoubleValue(row.getAs(field.name())?:0.0)
                    DataTypes.ByteType  -> Value.newBuilder().setBytesValue(row.getAs(field.name())?: ByteString.EMPTY)
                    else -> Value.newBuilder().setStringValue(row.getAs(field.name())?:"")
                }
                rowBuilder.addValues(valueBuilder)
            }
            responseBuilder.addRows(rowBuilder)
        }
        return responseBuilder.build()
    }

    private fun convert(schema: StructType): Schema{
        val schemaBuilder = Schema.newBuilder()

        schema.fields().forEach {
            field ->
                val type = when(field.dataType()){
                    DataTypes.BooleanType  -> FieldSchema.Type.BOOLEAN
                    DataTypes.IntegerType  -> FieldSchema.Type.INT
                    DataTypes.LongType  -> FieldSchema.Type.LONG
                    DataTypes.FloatType  -> FieldSchema.Type.FLOAT
                    DataTypes.DoubleType  -> FieldSchema.Type.DOUBLE
                    DataTypes.StringType  -> FieldSchema.Type.STRING
                    DataTypes.ByteType  -> FieldSchema.Type.BYTES
                    else -> FieldSchema.Type.UNRECOGNIZED
                }
                schemaBuilder.addFields(FieldSchema.newBuilder().setName(field.name()).setType(type))
        }
        return schemaBuilder.build()
    }

    private fun createBusinessDF(spark: SparkSession): Dataset<Row> {
        val data = listOf(
                RowFactory.create("ACBHG003", "Convertibles Business"),
                RowFactory.create("ACTHG003", "Credit Business"),
                RowFactory.create("AMGHG003", "Management"),
                RowFactory.create("AVTHG003", "Equity/Fx Volatility Business"),
                RowFactory.create("AMTHG003", "Rates Business")
        )


        val schema = DataTypes.createStructType(
                listOf(
                        DataTypes.createStructField("SMOBookID", DataTypes.StringType, true),
                        DataTypes.createStructField("Business", DataTypes.StringType, true)
                )
        )

        return spark.createDataFrame(data, schema)

    }

    override fun start() {
        refresh()
    }

    override fun stop() {
        spark.stop()
    }

}