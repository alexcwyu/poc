package com.bfam.riskreport

import com.bfam.riskreport.model.Value
import com.bfam.riskreport.service.FieldSchema
import com.bfam.riskreport.service.ReportResponse
import com.bfam.riskreport.service.Schema
import com.bfam.riskreport.service.Type
import com.databricks.spark.avro.MySchemaConversions
import com.databricks.spark.avro.SchemaConverters
import com.google.common.collect.Lists
import com.google.protobuf.ByteString
import org.apache.avro.SchemaBuilder
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumReader
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.types.StructType
import org.openjdk.jmh.annotations.*
import java.io.ByteArrayOutputStream
import kotlin.system.measureTimeMillis


open class SparkRiskRunner {

    open class Session {
        lateinit var spark: SparkSession
        lateinit var businessDF: Dataset<Row>
        lateinit var currencyDF: Dataset<Row>
        lateinit var ulCurrencyDF: Dataset<Row>
        lateinit var riskDF: Dataset<Row>
        lateinit var instDF: Dataset<Row>
        lateinit var portfolioDF: Dataset<Row>
        lateinit var bigJoin: Dataset<Row>

        @Setup(Level.Invocation)
        fun init() {
            val sparkConf = SparkConf()
            sparkConf.set("spark.sql.shuffle.partitions", "2")
            sparkConf.set("spark.default.parallelism", "4")
            sparkConf.set("spark.sql.tungsten.enabled", "true")
            sparkConf.set("spark.sql.codegen", "true")
            //sparkConf.set("spark.cores.max", "8")
            //sparkConf.set("spark.io.compression.codec", "snappy")
            //sparkConf.set("spark.streaming.backpressure.enabled", "true")
            //conf.set("spark.executor.cores", "4")


            spark = SparkSession
                    .builder()
                    .master("local")
                    .appName("SparkRiskApp")
                    .config(sparkConf)
                    .getOrCreate()


            businessDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/business.parquet")
            businessDF = businessDF.columns().fold(businessDF) { newDF, col -> newDF.withColumnRenamed(col, col.replace(".", "_")) }

            currencyDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/currency.parquet")
            currencyDF = currencyDF.columns().fold(currencyDF) { newDF, col -> newDF.withColumnRenamed(col, col.replace(".", "_")) }

            instDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/inst.parquet")
            instDF = instDF.columns().fold(instDF) { newDF, col -> newDF.withColumnRenamed(col, col.replace(".", "_")) }

            portfolioDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/portfolio.parquet")
            portfolioDF = portfolioDF.columns().fold(portfolioDF) { newDF, col -> newDF.withColumnRenamed(col, col.replace(".", "_")) }

            riskDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/risk.parquet")
            riskDF = riskDF.columns().fold(riskDF) { newDF, col -> newDF.withColumnRenamed(col, col.replace(".", "_")) }

            ulCurrencyDF = currencyDF.columns().fold(currencyDF) { newDF, col -> newDF.withColumnRenamed(col, "ul_" + col.toLowerCase()) }


            bigJoin = riskDF
                    .join(instDF, riskDF.col("`risk_instrument_id`").equalTo(instDF.col("`inst_id`")), "left_outer")
                    .join(portfolioDF, riskDF.col("`risk_folio_id`").equalTo(portfolioDF.col("`portfolio_id`")), "left_outer")
                    .join(businessDF, riskDF.col("`risk_smo_book`").equalTo(businessDF.col("`business_id`")), "left_outer")
                    .join(currencyDF, instDF.col("`inst_ccy_id`").equalTo(currencyDF.col("`currency_id`")), "left_outer")
                    .join(ulCurrencyDF, instDF.col("`inst_ul_ccy_id`").equalTo(ulCurrencyDF.col("`ul_currency_id`")), "left_outer")

            bigJoin.cache()
            bigJoin.createOrReplaceTempView("bigjoin")

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5)
    fun groupby_2level(session: Session) {

        val query = """
            SELECT `portfolio_name`, `inst_name`, FIRST(`risk_mark`) as `risk_mark`, FIRST(`risk_underlying_spot`) as `risk_underlying_spot`,
            SUM(`risk_delta_cash_usd`) as `risk_delta_cash_usd`, SUM(`risk_gamma_cash_usd`) as `risk_gamma_cash_usd`, SUM(`risk_asset_value`) as `risk_asset_value`
            FROM bigjoin
            WHERE `business_id` = 'AVTHG003'
            GROUP BY `portfolio_name`, `inst_name`
            """.trimIndent()
        var df: Dataset<Row>? = null
        var response: ReportResponse? = null
        var df2: Dataset<Row>? = null
        var ba: ByteArray? = null
        val queryPerf = measureTimeMillis {

            df = session.spark.sql(query)
            df!!.show()
        }
        val covPerf = measureTimeMillis {

            response = convert(df!!)
        }
        val encodePerf = measureTimeMillis {

            ba = encode(df!!)
        }
        val decodePerf = measureTimeMillis {

            df2 = decode(ba!!, session.spark, df!!.schema())
        }
        println("-------forLoopMillisElapsed: $queryPerf $covPerf $encodePerf $decodePerf")
    }


    fun encode(df: Dataset<Row>): ByteArray {

        val recordBuilder = SchemaBuilder.record("DataFrame").namespace("com.bfam.riskreport")
        val schema = SchemaConverters.convertStructToAvro(df.schema(), recordBuilder, "com.bfam.riskreport")

        val writer = SpecificDatumWriter<GenericRecord>(schema)
        val out = ByteArrayOutputStream()
        val encoder = EncoderFactory.get().binaryEncoder(out, null)

        df.collectAsList().forEach { row ->
            val gr: GenericRecord = GenericData.Record(schema)
            row.schema().fieldNames().forEach { name -> gr.put(name, row.getAs(name)) }
            writer.write(gr, encoder)
        }
        encoder.flush()
        out.close()

        return out.toByteArray()
    }

    fun decode(bytes: ByteArray, sparkMgr: SparkSession, sparkschema: StructType): Dataset<Row> {
        val recordBuilder = SchemaBuilder.record("DataFrame").namespace("com.bfam.riskreport")
        val schema = SchemaConverters.convertStructToAvro(sparkschema, recordBuilder, "com.bfam.riskreport")

        val datumReader = SpecificDatumReader<GenericRecord>(schema)
        val decoder = DecoderFactory.get().binaryDecoder(bytes, null)

        val myAvroType = SchemaConverters.toSqlType(schema).dataType()
        val myAvroRecordConverter = MySchemaConversions.createConverterToSQL(schema, myAvroType)
        val records = Lists.newArrayList<Row>()

        while (!decoder.isEnd) {
            records.add(myAvroRecordConverter.apply(datumReader.read(null, decoder)))
        }
        return sparkMgr.createDataFrame(records, sparkschema)
    }

    private fun convert(df: Dataset<Row>): ReportResponse? {
        val responseBuilder = ReportResponse.newBuilder()
        responseBuilder.schema = convert(df.schema())
        df.collectAsList().forEach { row ->
            val rowBuilder = com.bfam.riskreport.model.Row.newBuilder()
            row.schema().fields().forEach { field ->
                val valueBuilder = when (field.dataType()) {
                    DataTypes.BooleanType -> Value.newBuilder().setBoolValue(row.getAs(field.name()) ?: false)
                    DataTypes.IntegerType -> Value.newBuilder().setIntValue(row.getAs(field.name()) ?: 0)
                    DataTypes.LongType -> Value.newBuilder().setLongValue(row.getAs(field.name()) ?: 0)
                    DataTypes.FloatType -> Value.newBuilder().setFloatValue(row.getAs(field.name()) ?: 0.0f)
                    DataTypes.DoubleType -> Value.newBuilder().setDoubleValue(row.getAs(field.name()) ?: 0.0)
                    DataTypes.ByteType -> Value.newBuilder().setBytesValue(row.getAs(field.name()) ?: ByteString.EMPTY)
                    else -> Value.newBuilder().setStringValue(row.getAs(field.name()) ?: "")
                }
                rowBuilder.addValues(valueBuilder)
            }
            responseBuilder.addRows(rowBuilder)
        }
        return responseBuilder.build()
    }

    private fun convert(schema: StructType): Schema {
        val schemaBuilder = Schema.newBuilder()

        schema.fields().forEach { field ->
            val type = when (field.dataType()) {
                DataTypes.BooleanType -> Type.BOOLEAN
                DataTypes.IntegerType -> Type.INT
                DataTypes.LongType -> Type.LONG
                DataTypes.FloatType -> Type.FLOAT
                DataTypes.DoubleType -> Type.DOUBLE
                DataTypes.StringType -> Type.STRING
                DataTypes.ByteType -> Type.BYTES
                else -> Type.UNRECOGNIZED
            }
            schemaBuilder.addFields(FieldSchema.newBuilder().setName(field.name()).setType(type))
        }
        return schemaBuilder.build()
    }
}

fun main(args: Array<String>) {

    val runner = SparkRiskRunner()
    val session = SparkRiskRunner.Session()
    session.init()

    (0..10).forEach {
        runner.groupby_2level(session)
    }
}