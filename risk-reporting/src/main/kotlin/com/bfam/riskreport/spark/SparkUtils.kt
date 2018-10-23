package com.bfam.riskreport.spark

import com.databricks.spark.avro.MySchemaConversions
import com.databricks.spark.avro.SchemaConverters
import com.google.common.collect.Lists
import org.apache.avro.Schema
import org.apache.avro.SchemaBuilder
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumReader
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import java.io.ByteArrayOutputStream


fun loadCSV(spark: SparkSession, path: String, header: String = "true"): Dataset<Row> =
        spark.read().format("csv")
                .option("inferSchema", "true")
                .option("header", header)
                .load(path)


fun loadExcel(spark: SparkSession, path: String, schema: StructType): Dataset<Row> =
        spark.sqlContext().read()
                .format("com.crealytics.spark.excel")
                .option("location", path)
                .option("sheetName", "Sheet1") // Required
                .option("useHeader", "true") // Required
                .option("treatEmptyValuesAsNulls", "true") // Optional, default: true
                .option("inferSchema", "true") // Optional, default: false
                .option("addColorColumns", "false") // Optional, default: false
                .option("startColumn", 0) // Optional, default: 0
                .option("endColumn", 100) // Optional, default: Int.MaxValue
                .option("timestampFormat", "MM-dd-yyyy HH:mm:ss") // Optional, default: yyyy-mm-dd hh:mm:ss[.fffffffff]
                .option("maxRowsInMemory", 20) // Optional, default None. If set, uses a streaming reader which can help with big files
                .option("excerptSize", 10) // Optional, default: 10. If set and if schema inferred, number of rows to infer schema from
                .schema(schema) // Optional, default: Either inferred schema, or all columns are Strings
                .load()


fun loadOracleTable(spark: SparkSession, url:String, table: String, user: String, password: String, driver : String): Dataset<Row> =
        spark.read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", table)
                .option("user", user)
                .option("password", password)
                .option("driver", driver)
                .load()


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

fun decode(bytes: ByteArray, spark: SparkSession, sparkschema: StructType): Dataset<Row> {
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
    return spark.createDataFrame(records, sparkschema)
}
