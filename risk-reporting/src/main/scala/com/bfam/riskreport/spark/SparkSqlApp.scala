//package com.bfam.riskreport.spark
//
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.types.{StructField, StructType, StringType, LongType, Metadata}
//import org.apache.spark.sql.functions.{col, column, expr, lit, get_json_object, json_tuple, udf}
//
//object SparkSqlApp {
//
//  def main(args: Array[String]): Unit = {
//    //create spark
//    val spark = SparkSession
//      .builder
//      .master("local")
//      .appName("Test")
//      .config("spark.jars", "/mnt/data/dev/workspaces/poc/bigdata-poc/lib/postgresql-42.2.2.jar")
//      .getOrCreate()
//
//    //spark context
//    val sc = spark.sparkContext
//
//    val jdbcHostname = "localhost"
//    val jdbcPort = 5432
//    val jdbcDatabase ="algo"
//    val jdbcUsername ="algo"
//    val jdbcPassword ="algo"
//    val driverClass = "org.postgresql.Driver"
//    val jdbcUrl = s"jdbc:postgresql://${jdbcHostname}:${jdbcPort}/${jdbcDatabase}"
//
//    // Create a Properties() object to hold the parameters.
//    import java.util.Properties
//    val connectionProperties = new Properties()
//    connectionProperties.put("user", s"${jdbcUsername}")
//    connectionProperties.put("password", s"${jdbcPassword}")
//    connectionProperties.put("Driver", s"${driverClass}")
////
////    val positions_table = spark.read.jdbc(jdbcUrl, "positions", connectionProperties)
////    val accounts_table = spark.read.jdbc(jdbcUrl, "accounts", connectionProperties)
////
////    positions_table.printSchema
////
////    val result = positions_table.select("acct_id", "total_qty * avg_price as val").groupBy("acct_id").sum("val")
//
//
//    def power3(number: Double) : Double = number * number * number
//
//    val udfExampleDF = spark.range(5).toDF("num")
//
//    val power3udf = udf(power3(_:Double):Double)
//
//    print(udfExampleDF.select(power3udf(col("num"))))
//    spark.udf.register("power3", power3(_:Double):Double)
//    print(udfExampleDF.selectExpr("power3(num)").show())
//    spark.stop()
//  }
//}
