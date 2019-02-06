package com.bfam.kakfa.publisher

import com.bfam.kakfa.GsonSerializer
import com.bfam.model.Instrument
import com.bfam.model.MarketPrice
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*


import org.apache.logging.log4j.LogManager;


class InstrumentPublisher(brokers: String, val topic: String){
    private val logger = LogManager.getLogger(InstrumentPublisher::class.java)
    val producer : KafkaProducer<String, Instrument>

    init{
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.canonicalName
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = GsonSerializer::class.java.canonicalName
        this.producer = KafkaProducer(props)
    }

    fun publish(){
        val rnd = Random()
        for (stock in stockNames) {
            val inst = Instrument(stock.key, stock.value, "Stock")
            publish(inst)

            Thread.sleep(rnd.nextInt(2000).toLong() + 1000)
        }
    }

    fun publish(inst: Instrument) {
        val futureResult = producer.send(ProducerRecord(topic, inst.symbol, inst))
        futureResult.get()
    }
}
