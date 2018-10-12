package info.alexyu.spark

import org.apache.spark.sql.SparkSession

case class Person(name: String, age: Int)

case class Bank(age: Integer, job: String, marital: String, education: String, balance: Integer)


object ScalaSparkApp {


  def main(args: Array[String]): Unit = {
    //create spark
    val spark = SparkSession
      .builder
      .master("local")
      .appName("Test")
      .config("spark.jars", "/mnt/data/dev/workspaces/poc/bigdata-poc/lib/postgresql-42.2.2.jar")
      .getOrCreate()

    //spark context
    val sc = spark.sparkContext

    val jdbcHostname = "localhost"
    val jdbcPort = 5432
    val jdbcDatabase ="algo"
    val jdbcUsername ="algo"
    val jdbcPassword ="algo"
    val driverClass = "org.postgresql.Driver"
    val jdbcUrl = s"jdbc:postgresql://${jdbcHostname}:${jdbcPort}/${jdbcDatabase}"

    // Create a Properties() object to hold the parameters.
    import java.util.Properties
    val connectionProperties = new Properties()
    connectionProperties.put("user", s"${jdbcUsername}")
    connectionProperties.put("password", s"${jdbcPassword}")
    connectionProperties.put("Driver", s"${driverClass}")

    val positions_table = spark.read.jdbc(jdbcUrl, "positions", connectionProperties)
    val accounts_table = spark.read.jdbc(jdbcUrl, "accounts", connectionProperties)

    positions_table.printSchema

    val result = positions_table.select("acct_id", "total_qty * avg_price as val").groupBy("acct_id").sum("val")

    spark.stop()
  }
}
