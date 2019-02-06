package com.bfam.kakfa

import com.bfam.model.MarketPrice
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.logging.log4j.LogManager
import java.util.*


private val logger = LogManager.getLogger(PriceConsumer::class.java)

class PriceConsumer(brokers: String, val topic: String){
    val consumer : KafkaConsumer<String, MarketPrice>

    init{
        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.canonicalName
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = GsonDeserializer::class.java.canonicalName
        props[GsonDeserializer.CONFIG_VALUE_CLASS] = MarketPrice::class.java.canonicalName
        props[ConsumerConfig.GROUP_ID_CONFIG] = "price-consumer"
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"
        this.consumer = KafkaConsumer(props)
    }


    fun subscribe(){
        consumer.subscribe(listOf(topic))

        while (true) {
            val consumerRecords = consumer.poll(1000)

            if (consumerRecords.count() == 0) {
                continue
            }
            consumerRecords.forEach{p -> logger.info("received {}, {}", p.key(), p.value())}
        }
        // consumer.close()
    }
}


fun main(args: Array<String>) {
   val consumer = PriceConsumer("localhost:29092", "price-stream")
    consumer.subscribe()
}