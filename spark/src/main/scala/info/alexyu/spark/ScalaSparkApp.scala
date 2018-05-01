package info.alexyu.spark

import java.net.URL
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

case class Person(name: String, age: Int)

case class Bank(age: Integer, job: String, marital: String, education: String, balance: Integer)




object ScalaSparkApp {
  

  def structuredStreaming(spark: SparkSession, sc: SparkContext): Unit = {
    val schema = StructType(
      StructField("id", LongType, nullable = false) ::
        StructField("name", StringType, nullable = false) ::
        StructField("score", DoubleType, nullable = false) :: Nil)

  }

  def main(args: Array[String]): Unit = {
    //create spark
    val spark = SparkSession
      .builder
      .master("local")
      .appName("Test")
      .getOrCreate()

    //spark context
    val sc = spark.sparkContext

    //sql context
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import spark.implicits._

    val peopleRDD: RDD[Person] = spark.sparkContext.parallelize(Seq(Person("Jacek", 10)))


    val bankText = sc.parallelize(
      IOUtils.toString(
        new URL("https://s3.amazonaws.com/apache-zeppelin/tutorial/bank/bank.csv"),
        Charset.forName("utf8")).split("\n"))


    val bank = bankText.map(s => s.split(";")).filter(s => s(0) != "\"age\"").map(
      s => Bank(s(0).toInt,
        s(1).replaceAll("\"", ""),
        s(2).replaceAll("\"", ""),
        s(3).replaceAll("\"", ""),
        s(5).replaceAll("\"", "").toInt
      )
    ).toDF()

    bank.registerTempTable("bank")

    val jobs = bank.where('balance >= 100).where('age >= 20).select('job).as[String]
    println(jobs)

    val teenagers = sqlContext.sql("SELECT * FROM bank")

    teenagers.show()

    spark.stop()
  }
}
