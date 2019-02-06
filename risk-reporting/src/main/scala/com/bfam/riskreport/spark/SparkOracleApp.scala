package com.bfam.riskreport.spark

import org.apache.spark.sql.SparkSession

case class Person(name: String, age: Int)

case class Bank(age: Integer, job: String, marital: String, education: String, balance: Integer)


object SparkOracleApp {


  def main(args: Array[String]): Unit = {
    //create spark
    val spark = SparkSession
      .builder
      .master("local")
      .appName("Test")
      //.config("spark.jars", "C:\\Users\\AYu\\Documents\\workspaces\\bfam\\risk-reporting\\libs\\ojdbc6.jar")
      .getOrCreate()

    //spark context
    val sc = spark.sparkContext

    val jdbcHostname = "bafhdrordbuat01"
    val jdbcPort = 1521
    val jdbcDatabase ="TKSPSD01"
    val jdbcUsername ="sophissit"
    val jdbcPassword ="soph15u4t"
    val driverClass = "oracle.jdbc.driver.OracleDriver"
    val jdbcUrl = s"jdbc:oracle:thin:${jdbcUsername}/${jdbcPassword}@//${jdbcHostname}:${jdbcPort}/${jdbcDatabase}"

    val folio_table = spark.read
      .format("jdbc")
      .option("url", jdbcUrl)
      .option("dbtable", "SOPHISSIT.FOLIO")
      .option("user", jdbcUsername)
      .option("password", jdbcPassword)
      .option("driver", driverClass)
      .load()


    val result = folio_table.select("NAME", "IDENT")

    result.show()
    spark.stop()
  }
}
