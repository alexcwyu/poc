package com.bfam.riskreport.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types._

object HelloSpark {

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .master("local")
      .appName("SparkSQL")
      //.config("spark.some.config.option", "some-value")
      .getOrCreate()
    val sc =  spark.sparkContext


    val dailyRiskSchema = StructType(List(
      StructField("_name", StringType, nullable = true),
      StructField("GOG Delta Cash no-beta", DoubleType, nullable = true),
      StructField("GOG Cash Gamma (USD)", DoubleType, nullable = true),
      StructField("Vega curr. global", DoubleType, nullable = true),
      StructField("Theta curr. global", DoubleType, nullable = true),
      StructField("GOG IR01 (USD)", DoubleType, nullable = true),
      StructField("GOG CR01 (USD)", DoubleType, nullable = true),
      StructField("GOG PV10% Risky", DoubleType, nullable = true),
      StructField("GOG VoD0 (USD)", DoubleType, nullable = true),
      StructField("GOG RR01 (USD)", DoubleType, nullable = true),
      StructField("Issuer", StringType, nullable = true),
      StructField("Number of securities", DoubleType, nullable = true),
      StructField("Asset Value curr. global", DoubleType, nullable = true),
      StructField("Strike", DoubleType, nullable = true),
      StructField("Underlying Spot", DoubleType, nullable = true),
      StructField("Delta", DoubleType, nullable = true),
      StructField("Delta Cash", DoubleType, nullable = true),
      StructField("FX Rate", DoubleType, nullable = true),
      StructField("GOG FX", DoubleType, nullable = true),
      StructField("FX Delta", DoubleType, nullable = true),
      StructField("TYPESICO", StringType, nullable = true),
      StructField("Notional (bonds)", DoubleType, nullable = true),
      StructField("Notional (swaps)", DoubleType, nullable = true),
      StructField("Clearing ID", StringType, nullable = true),
      StructField("Contract size", DoubleType, nullable = true),
      StructField("Instrument type", StringType, nullable = true),
      StructField("GOG Instrument Name", StringType, nullable = true),
      StructField("Epsilon curr. global", DoubleType, nullable = true),
      StructField("Weighted Vega", DoubleType, nullable = true),
      StructField("Beta", DoubleType, nullable = true),
      StructField("GOG CB Premium (USD)", DoubleType, nullable = true),
      StructField("Delta curr. global", DoubleType, nullable = true),
      StructField("Delta long", DoubleType, nullable = true),
      StructField("Delta short", DoubleType, nullable = true),
      StructField("Vega long", DoubleType, nullable = true),
      StructField("Vega short", DoubleType, nullable = true),
      StructField("REF:RIC", StringType, nullable = true),
      StructField("GOG_IT Portfolio/Position Code", IntegerType, nullable = true),
      StructField("SMOBook", StringType, nullable = true),
      StructField("Allotment", StringType, nullable = true),
      StructField("Currency", StringType, nullable = true),
      StructField("Nominal", DoubleType, nullable = true),
      StructField("GOG MARK (DLY)", DoubleType, nullable = true),
      StructField("Underlying Currency", StringType, nullable = true),
      StructField("Instrument code", IntegerType, nullable = true),
      StructField("Folio Path (SLOW!!)", StringType, nullable = true)
    ))


    val dailyRiskDF = spark.sqlContext.read
      .format("com.crealytics.spark.excel")
      .option("location", "C:\\Users\\AYu\\Documents\\workspaces\\bfam\\risk-reporting\\src\\main\\resources\\dashboard\\Risk_Dashboard_AOF_20181020.xlsx")
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
      //.schema(dailyRiskSchema) // Optional, default: Either inferred schema, or all columns are Strings
      .load()


    dailyRiskDF.printSchema()

    dailyRiskDF.show(10)


  }

}