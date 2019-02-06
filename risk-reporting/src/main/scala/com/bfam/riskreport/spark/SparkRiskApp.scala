package com.bfam.riskreport.spark

import java.io.ByteArrayOutputStream

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import com.databricks.spark.avro._
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.avro.{Schema, SchemaBuilder}
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io._
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}
import org.apache.avro.generic.GenericRecord
import com.databricks.spark.avro.SchemaConverters
import com.google.common.collect.Lists

object SparkRiskApp {

  val jdbcHostname = "bafhdrordbuat01"
  val jdbcPort = 1521
  val jdbcDatabase = "TKSPSD01"
  val jdbcUsername = "sophissit"
  val jdbcPassword = "soph15u4t"
  val driverClass = "oracle.jdbc.driver.OracleDriver"
  val jdbcUrl = s"jdbc:oracle:thin:${jdbcUsername}/${jdbcPassword}@//${jdbcHostname}:${jdbcPort}/${jdbcDatabase}"

  val ccySql =
    s"""
SELECT
SICOVAM AS DEVISE, LIBELLE, DECODE(INVERSERRIC, 1, 1/D, D) AS COURS, JOUR AS T
FROM HISTORIQUE, DEVISEV2
WHERE SICOVAM = CODE
AND D IS NOT NULL AND D > 0
AND JOUR = TRUNC(SYSDATE) - ?
"""

  val folioSql =
    s"""
SELECT FOLIO.IDENT, FOLIO.NAME, FOLIO.MGR, FOLIO.INFOS, FOLIO.SICOVAM, FOLIO.TYPE, FOLIO.LIMITE_PERTE, FOLIO.ENTITE, FOLIO.MARKET, FOLIO.QUOTATION, FOLIO.HEDGEFUND, FOLIO.CURRENCY, FOLIO.BUSINESS_LINE_ID, FOLIO.MARKED_AS_CLOSED
FROM SOPHISPROD.FOLIO FOLIO
ORDER BY FOLIO.IDENT"""

  val instSql =
    s"""
SELECT TT.SICOVAM,
       CASE WHEN TT.TYPE <> 'M' THEN TT.LIBELLE ELSE
         (SELECT EXTENDEDMNEMO FROM MO_SUPPORT M WHERE M.MNEMO = TT.CODESJ) ||
         ' ' || TO_CHAR(TT.MATURITY, 'DD/MM/YY') || ' ' || DECODE(TT.TYPEPRO, 2, 'C', 'P') || ' '|| TT.PRIXEXER
       END LIBELLE,
       TT.NOMINAL,
       TT.RATIO,
       TT.TYPE,
       TT.MATURITY,
       TT.CURRENCY,
       (SELECT DECODE(TT.TYPE,'H',NULL,TR.SERVISEN) FROM RIC TR WHERE TR.SICOVAM = TT.SICOVAM) BLOOMBERG,
       CASE WHEN LENGTH(NVL(TT.MNEMO_V2,TT.REFERENCE)) >= 12
       		THEN SUBSTR(NVL(TT.MNEMO_V2,TT.REFERENCE),1,12) ELSE NULL END ISIN,
       (SELECT NAME
		FROM SECTORS SS,
			SECTOR_INSTRUMENT_ASSOCIATION SI
		WHERE SI.TYPE = (SELECT ID FROM SECTORS WHERE PARENT = 0 AND NAME = 'Bloomberg')
		AND SS.ID = SI.SECTOR
		AND TT.SICOVAM = SI.SICOVAM) SECTOR,
  (SELECT SS.SECTORPATH
		FROM (select ID, NAME, SYS_CONNECT_BY_PATH(NAME, '|') as SECTORPATH from SECTORS start with PARENT = 0 connect by prior ID = PARENT) SS,
			SECTOR_INSTRUMENT_ASSOCIATION SI
		WHERE SI.TYPE = (SELECT ID FROM SECTORS WHERE PARENT = 0 AND NAME = 'Bloomberg')
		AND SS.ID = SI.SECTOR
		AND TT.SICOVAM = SI.SICOVAM) FULLSECTORS,
       (SELECT DESCRIPTION
		FROM SECTORS SS,
			SECTOR_INSTRUMENT_ASSOCIATION SI
		WHERE SI.TYPE = (SELECT ID FROM SECTORS WHERE PARENT = 0 AND NAME = 'Reuters Country')
		AND SS.ID = SI.SECTOR
		AND TT.SICOVAM = SI.SICOVAM) COUNTRY,
	  (SELECT NAME
	     FROM RATING_SCALE RR,
	          RATING_INSTRUMENT_ASSOCIATION RI
	     WHERE SOURCE_IDENT = 21
	     AND   AGENCY = 21
	     AND   SENIORITY = 0
	     AND   RR.ID = RI.SCALE
	     AND   TT.SICOVAM = RI.SICOVAM) RATING,
       UT.SICOVAM UL_SICOVAM,
       UT.LIBELLE UL_LIBELLE,
       UT.TYPE UL_TYPE,
       UT.DEVISECTT UL_CURRENCY,
       (SELECT DECODE(UT.TYPE,'H',NULL,UR.SERVISEN) FROM RIC UR WHERE UR.SICOVAM = UT.SICOVAM) UL_BLOOMBERG,
       CASE WHEN LENGTH(NVL(UT.MNEMO_V2,UT.REFERENCE)) >= 12
       		THEN SUBSTR(NVL(UT.MNEMO_V2,UT.REFERENCE),1,12) ELSE NULL END UL_ISIN,
       (SELECT NAME
		FROM SECTORS SS,
			SECTOR_INSTRUMENT_ASSOCIATION SI
		WHERE SI.TYPE = (SELECT ID FROM SECTORS WHERE PARENT = 0 AND NAME = 'Bloomberg')
		AND SS.ID = SI.SECTOR
		AND UT.SICOVAM = SI.SICOVAM) UL_SECTOR,
       (SELECT DESCRIPTION
		FROM SECTORS SS,
			SECTOR_INSTRUMENT_ASSOCIATION SI
		WHERE SI.TYPE = (SELECT ID FROM SECTORS WHERE PARENT = 0 AND NAME = 'Reuters Country')
		AND SS.ID = SI.SECTOR
		AND UT.SICOVAM = SI.SICOVAM) UL_COUNTRY,
	  (SELECT NAME
	     FROM RATING_SCALE RR,
	          RATING_INSTRUMENT_ASSOCIATION RI
	     WHERE SOURCE_IDENT = 21
	     AND   AGENCY = 21
	     AND   SENIORITY = 0
	     AND   RR.ID = RI.SCALE
	     AND   UT.SICOVAM = RI.SICOVAM) UL_RATING
FROM (SELECT T.LIBELLE,
             T.NOMINAL,
             T.SICOVAM,
             T.MNEMO_V2,
             T.REFERENCE,
             T.PRIXEXER,
             T.TYPEPRO,
             T.CODESJ,
             DECODE(T.QUOTITE,
                   0, DECODE(T.TYPE, 'S', 1, NULL),
                   T.QUOTITE
             ) QUOTITE,
             CASE
               WHEN T.TYPE = 'D' AND T.TYPEPRO = 3 THEN 'CB'
               ELSE T.TYPE
             END TYPE,
             DECODE(T.TYPE,
                   'M', 1,
                   'K', 1,
                   'F', 1,
                   'P', 1,
                   T.PARITE / DECODE(T.PROPORTION, 0, NULL, T.PROPORTION)
             ) RATIO,
             DECODE(T.TYPE,
                   'M', T.FINPER,
                   'D', T.FINPER,
                   'O', T.FINPER,
                   'F', T.ECHEANCE,
                   'K', T.ECHEANCE,
                   'S', T.DATEFINAL,
                   NULL
             ) MATURITY,
             T.DEVISECTT CURRENCY,
             P.UL_SICOVAM
      FROM TITRES T,
           (SELECT DISTINCT SICOVAM,
                   UL_SICOVAM
            FROM (SELECT SICOVAM,
                         DECODE(TYPE,
                               'M', (SELECT M.SICOVAM FROM MO_SUPPORT M WHERE CODESJ = M.MNEMO),
                               'F', CODE_EMET,
                               'K', CODE_EMET,
                               'P', CODE_EMET,
                               'O', CODE_EMET,
                               'S', NVL(J1REFCON2, J2REFCON2),
                               CODESJ
                         ) UL_SICOVAM
                  FROM TITRES) T
            START WITH SICOVAM IN (SELECT DISTINCT H.SICOVAM
            FROM POSITION H,
                 (SELECT IDENT
                  FROM FOLIO
                  START WITH NAME = 'AOF' AND MGR = 1
                  CONNECT BY PRIOR IDENT = MGR) F
            WHERE H.OPCVM = F.IDENT) CONNECT BY SICOVAM = PRIOR UL_SICOVAM
            UNION
            SELECT SICOVAM,
                   NULL
            FROM TITRES
            WHERE TYPE IN ('X', 'E')) P
      WHERE T.SICOVAM = P.SICOVAM) TT,
     TITRES UT
WHERE TT.UL_SICOVAM = UT.SICOVAM (+)
ORDER BY TT.SICOVAM
    """

  def loadExcel(spark: SparkSession, path: String, schema : StructType): DataFrame =
    spark.sqlContext.read
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

  def loadCSV(spark: SparkSession, path: String, header: String = "true"): DataFrame =
    spark.read.format("csv")
      .option("inferSchema", "true")
      .option("header", header)
      .load(path)

  def loadOracleTable(spark: SparkSession, table: String): DataFrame =
    spark.read
      .format("jdbc")
      .option("url", jdbcUrl)
      .option("dbtable", table)
      .option("user", jdbcUsername)
      .option("password", jdbcPassword)
      .option("driver", driverClass)
      .load()


  def createBusinessDF(spark: SparkSession): DataFrame = {

    val data = Seq(
      Row("ACBHG003", "Convertibles Business"),
      Row("ACTHG003", "Credit Business"),
      Row("AMGHG003", "Management"),
      Row("AVTHG003", "Equity/Fx Volatility Business"),
      Row("AMTHG003", "Rates Business")
    )


    val schema = StructType(
      List(
        StructField("SMOBookID", StringType, true),
        StructField("Business", StringType, true)
      )
    )

    return spark.createDataFrame(
      spark.sparkContext.parallelize(data),
      schema
    )
  }

  def encode(df: DataFrame) : Array[Byte] =  {

    val recordBuilder = SchemaBuilder.record("DataFrame").namespace("com.bfam.riskreport")
    val schema = SchemaConverters.convertStructToAvro(df.schema, recordBuilder, "com.bfam.riskreport")

    val writer = new SpecificDatumWriter[GenericRecord](schema)
    val out = new ByteArrayOutputStream()
    val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)

    df.collect().foreach{ row =>
      val gr: GenericRecord = new GenericData.Record(schema)
      row.schema.fieldNames.foreach(name => gr.put(name, row.getAs(name)))
      writer.write(gr, encoder)
    }
    encoder.flush()
    out.close()

    out.toByteArray
  }


  def decode(bytes :  Array[Byte], spark: SparkSession, sparkschema: StructType): DataFrame = {

    val recordBuilder = SchemaBuilder.record("DataFrame").namespace("com.bfam.riskreport")
    val schema = SchemaConverters.convertStructToAvro(sparkschema, recordBuilder, "com.bfam.riskreport")

    val datumReader = new SpecificDatumReader[GenericRecord](schema)
    val decoder = DecoderFactory.get.binaryDecoder(bytes, null)

    val myAvroType = SchemaConverters.toSqlType(schema).dataType
    val myAvroRecordConverter = MySchemaConversions.createConverterToSQL(schema, myAvroType)
    val records = Lists.newArrayList[Row]()

    while (! decoder.isEnd) {
      records.add(myAvroRecordConverter.apply(datumReader.read(null, decoder)))
    }
    spark.createDataFrame(records, sparkschema)
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .master("local")
      .appName("SparkRiskApp")
      //.config("spark.jars", "C:\\Users\\AYu\\Documents\\workspaces\\bfam\\risk-reporting\\libs\\ojdbc6.jar")
      .getOrCreate()

    import org.apache.spark.sql.types._
    val riskSchema = StructType(Array(StructField("_name", StringType, true),
      StructField("GOG Delta Cash no-beta", DoubleType, true),
      StructField("GOG Cash Gamma (USD)", DoubleType, true),
      StructField("Vega curr. global", DoubleType, true),
      StructField("Theta curr. global", DoubleType, true),
      StructField("GOG IR01 (USD)", DoubleType, true),
      StructField("GOG CR01 (USD)", DoubleType, true),
      StructField("GOG PV10% Risky", DoubleType, true),
      StructField("GOG VoD0 (USD)", DoubleType, true),
      StructField("GOG RR01 (USD)", DoubleType, true),
      StructField("Issuer", StringType, true),
      StructField("Number of securities", DoubleType, true),
      StructField("Asset Value curr. global", DoubleType, true),
      StructField("Strike", DoubleType, true),
      StructField("Underlying Spot", DoubleType, true),
      StructField("Delta", DoubleType, true),
      StructField("Delta Cash", DoubleType, true),
      StructField("FX Rate", DoubleType, true),
      StructField("GOG FX", DoubleType, true),
      StructField("FX Delta", DoubleType, true),
      StructField("TYPESICO", DoubleType, true),
      StructField("Notional (bonds)", DoubleType, true),
      StructField("Notional (swaps)", DoubleType, true),
      StructField("Clearing ID", DoubleType, true),
      StructField("Contract size", DoubleType, true),
      StructField("Instrument type", StringType, true),
      StructField("GOG Instrument Name", StringType, true),
      StructField("Epsilon curr. global", DoubleType, true),
      StructField("Weighted Vega", DoubleType, true),
      StructField("Beta", DoubleType, true),
      StructField("GOG CB Premium (USD)", DoubleType, true),
      StructField("Delta curr. global", DoubleType, true),
      StructField("Delta long", DoubleType, true),
      StructField("Delta short", DoubleType, true),
      StructField("Vega long", DoubleType, true),
      StructField("Vega short", DoubleType, true),
      StructField("REF:RIC", StringType, true),
      StructField("GOG_IT Portfolio/Position Code", IntegerType, true),
      StructField("SMOBook", StringType, true),
      StructField("Allotment", StringType, true),
      StructField("Currency", StringType, true),
      StructField("Nominal", DoubleType, true),
      StructField("GOG MARK (DLY)", DoubleType, true),
      StructField("Underlying Currency", StringType, true),
      StructField("Instrument code", IntegerType, true),
      StructField("Folio Path (SLOW!!)", StringType, true)))

    val path = "/Users/alex/dev/workspaces/poc/risk-reporting/src/main/resources/"

    val riskDF = loadCSV(spark, path + "dashboard/risk.csv")
    val folioDF = loadCSV(spark, path + "dashboard/folio.csv")
    val instDF = loadCSV(spark, path + "dashboard/instrument.csv")
    //val instDF = loadOracleTable(spark, f"($instSql%s) instrument ")
    val currencyDF  = loadCSV(spark, path + "dashboard/currency.csv")
    //val currencyDF = loadOracleTable(spark, f"($ccySql%s) currency ")
    val businessDF = createBusinessDF(spark)

    riskDF.printSchema()
    folioDF.printSchema()
    instDF.printSchema()
    currencyDF.printSchema()
    businessDF.printSchema()

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


    val bigjoin = enrichedRiskDF.join(enrichedInstDF, enrichedRiskDF.col("Instrument code") === enrichedInstDF.col("Instrument SICOVAM"), "left_outer").
      join(enrichedCurrencyDF, enrichedRiskDF.col("Position CURRENCY") === enrichedCurrencyDF.col("DEVISE"), "left_outer").
      join(enrichedFlioDF, enrichedRiskDF.col("Folio") === enrichedFlioDF.col("IDENT"), "left_outer").
      join(businessDF, enrichedRiskDF.col("SMOBook") === businessDF.col("SMOBookID"), "left_outer")


   // bigjoin.createOrReplaceGlobalTempView("bigjoin")


    val data = encode(businessDF)
    val newDF = decode(data, spark, businessDF.schema)

    businessDF.show()
    newDF.show()

    spark.stop()
  }
}
