package com.bfam.kakfa.publisher

import avro.shaded.com.google.common.collect.Lists
import com.bfam.kakfa.GsonConsumer
import com.bfam.model.Instrument
import com.bfam.model.MarketPrice
import com.bfam.model.Portfolio
import com.bfam.model.Trade
import com.bfam.utils.MapBuilder
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import kotlin.concurrent.thread


val portfolioNames = MapBuilder<String, String>()
        .put("S01", "AsiaStrategy")
        .put("S02", "LongShort")
        .put("S03", "IndexArb")
        .put("S04", "FxVol")
        .put("S05", "EqVol")
        .put("S06", "StatsArb")
        .put("S07", "Credit")
        .put("S08", "Rate").build()

val portfolios = Lists.newArrayList(portfolioNames.keys)

val counterparties = Lists.newArrayList("JPMC", "MS", "DB", "CS", "BAML", "SG", "GS")

//val instruments = hashMapOf( "AAPL" to 188.0, "MSFT" to 105.61, "AMZN" to 1622.09, "GOOG" to 1034.01, "FB" to 141.99, "BABA" to 149.75, "INTC" to 47.27, "NVDA" to 199.82, "AMD" to 20.36)


val stockNames = MapBuilder<String, String>()
        .put("AAPL", "Apple")
        .put("MSFT", "Microsoft")
        .put("GOOGL", "Google A")
        .put("GOOG", "Google C")
        .put("FB", "Facebook")
        .put("CSCO", "Cisco Systems")
        .put("INTC", "Intel")
        .put("TSM", "Taiwan Semiconductor")
        .put("ORCL", "Oracle")
        .put("NVDA", "Nvida")
        .put("SAP", "SAP SE")
        .put("ADBE", "Adobe")
        .put("IBM", "International Business Machine")
        .put("CRM", "CRM Company")
        .put("QCOM", "QUALCOMM")
        .put("TXN", "Texas Instruments")
        .put("AVGO", "Broadcom")
        .put("ADP", "Automatic Data Processing")
        .put("VMW", "VMware")
        .put("BIDU", "Baidu Inc")
        .put("INTU", "Intuit Inc").build()

val stocksPrices = MapBuilder<String, Double>()
        .put("AAPL", 222.73)
        .put("MSFT", 108.1)
        .put("GOOGL", 1114.91)
        .put("GOOG", 1103.69)
        .put("FB", 154.39)
        .put("CSCO", 45.42)
        .put("INTC", 44.5)
        .put("TSM", 38.0)
        .put("ORCL", 47.99)
        .put("NVDA", 221.06)
        .put("SAP", 107.74)
        .put("ADBE", 251.76)
        .put("IBM", 131.21)
        .put("CRM", 140.66)
        .put("QCOM", 66.38)
        .put("TXN", 100.25)
        .put("AVGO", 229.05)
        .put("ADP", 142.44)
        .put("VMW", 145.41)
        .put("BIDU", 193.42)
        .put("INTU", 208.87).build()

val stocks = Lists.newArrayList(stocksPrices.keys)




fun main(args: Array<String>){

    val brokers = "localhost:29092"

    thread {
        val subscribe  = GsonConsumer(brokers, "inst", "inst-subscriber1", StringDeserializer::class.java, Instrument::class.java)
        subscribe.subscribe()
    }
    thread {
        val subscribe  = GsonConsumer(brokers, "portfolio", "portfolio-subscriber1", StringDeserializer::class.java, Portfolio::class.java)
        subscribe.subscribe()
    }
    thread {
        val subscribe  = GsonConsumer(brokers, "price", "price-subscriber1", StringDeserializer::class.java, MarketPrice::class.java)
        subscribe.subscribe()
    }
    thread {
        val subscribe = GsonConsumer(brokers, "trade", "trade-subscriber1", StringDeserializer::class.java, Trade::class.java)
        subscribe.subscribe()
    }

    thread {
        val instrumentPublisher = InstrumentPublisher(brokers)
        instrumentPublisher.publish()
    }
    thread {
        val portfolioPublisher = PortfolioPublisher(brokers)
        portfolioPublisher.publish()
    }
    thread {
        val pricePublisher = PricePublisher(brokers)
        pricePublisher.publish()
    }
    thread {
        val tradePublisher = TradePublisher(brokers)
        tradePublisher.publish()
    }

}