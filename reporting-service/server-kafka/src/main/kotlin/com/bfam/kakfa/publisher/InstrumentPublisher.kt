package com.bfam.kakfa

import avro.shaded.com.google.common.collect.Lists
import com.bfam.model.MarketPrice
import com.bfam.utils.MapBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*


import org.apache.logging.log4j.LogManager;




class PricePublisher(brokers: String, val topic: String){
    private val logger = LogManager.getLogger(PricePublisher::class.java)
    val producer : KafkaProducer<String, MarketPrice>

    init{
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.canonicalName
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = GsonSerializer::class.java.canonicalName
        this.producer = KafkaProducer(props)
    }

    fun publish(){
        val rnd = Random()
        while (true) {
            val stock = stocks[rnd.nextInt(stocks.size)]
            val lastPrice = stocksPrices[stock]
            val changePct = 1.0 - ((rnd.nextDouble() - 0.50)/ 10.0)
            val newPrice = lastPrice?:0 * changePct
            stocksPrices[stock] = newPrice
            val price = MarketPrice(stock, newPrice, System.currentTimeMillis())
            val futureResult = producer.send(ProducerRecord(topic, price.symbol, price))
            futureResult.get()

            Thread.sleep(rnd.nextInt(2000).toLong() + 1000)
        }
    }
}
