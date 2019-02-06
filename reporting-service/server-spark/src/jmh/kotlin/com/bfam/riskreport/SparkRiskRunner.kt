package com.bfam.riskreport

import org.apache.spark.SparkConf
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.openjdk.jmh.annotations.*


class SparkRiskRunner{
    @State(Scope.Benchmark)
    class Session{
        lateinit var spark : SparkSession
        lateinit var businessDF : Dataset<Row>
        lateinit var currencyDF : Dataset<Row>
        lateinit var ulCurrencyDF : Dataset<Row>
        lateinit var riskDF : Dataset<Row>
        lateinit var instDF : Dataset<Row>
        lateinit var portfolioDF : Dataset<Row>
        lateinit var bigJoin : Dataset<Row>

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
            currencyDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/currency.parquet")
            instDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/inst.parquet")
            portfolioDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/portfolio.parquet")
            riskDF = spark.read().parquet("/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/risk.parquet")
            ulCurrencyDF = currencyDF.columns().fold(currencyDF){newDF, col -> newDF.withColumnRenamed(col, "ul_"+col.toLowerCase())}


            bigJoin = riskDF
                    .join(instDF, riskDF.col("`risk.instrument_id`").equalTo(instDF.col("`inst.id`")), "left_outer")
                    .join(portfolioDF, riskDF.col("`risk.folio_id`").equalTo(portfolioDF.col("`portfolio.id`")), "left_outer")
                    .join(businessDF, riskDF.col("`risk.smo_book`").equalTo(businessDF.col("`business.id`")), "left_outer")
                    .join(currencyDF, instDF.col("`inst.ccy_id`").equalTo(currencyDF.col("`currency.id`")), "left_outer")
                    .join(ulCurrencyDF, instDF.col("`inst.ul_ccy_id`").equalTo(ulCurrencyDF.col("`ul_currency.id`")), "left_outer")

            bigJoin.cache()
            bigJoin.createOrReplaceTempView("bigjoin")

        }
    }
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5)
    fun groupby_2level(session : Session){
        val query = """
            SELECT `portfolio.name`, `inst.name`, FIRST(`risk.mark`), FIRST(`risk.underlying_spot`), SUM(`risk.delta_cash_usd`), SUM(`risk.gamma_cash_usd`), SUM(`risk.asset_value`)
            FROM bigjoin
            WHERE `business.id` = 'AVTHG003'
            GROUP BY `portfolio.name`, `inst.name`
            """.trimIndent()

        session.spark.sql(query).collect()
    }

}

fun main(args: Array<String>){

    val test = "com.bfam.riskreport.SparkRiskRunner"
    org.openjdk.jmh.Main.main(args)
}