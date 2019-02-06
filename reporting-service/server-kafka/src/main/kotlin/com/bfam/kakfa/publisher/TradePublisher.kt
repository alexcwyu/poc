package com.bfam.kakfa

import com.bfam.model.Trade
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.LongSerializer
import java.util.*

class TradePublisher(brokers: String, val topic: String){
    val producer : KafkaProducer<Long, Trade>

    init{
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.canonicalName
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = GsonSerializer::class.java.canonicalName
        this.producer = KafkaProducer(props)
    }

    fun publish(trade: Trade){
        val futureResult = producer.send(ProducerRecord(topic, trade.tradeId, trade))
        futureResult.get()
    }


}

fun main(args: Array<String>) {
    val publisher = TradePublisher("localhost:29092", "stock")
    //publisher.publish(null)
}