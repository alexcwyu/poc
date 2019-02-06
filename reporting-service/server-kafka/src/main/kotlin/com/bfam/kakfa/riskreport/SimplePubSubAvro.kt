package com.bfam.kakfa.riskreport

import com.bfam.kakfa.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import org.apache.parquet.avro.AvroParquetReader
import org.apache.avro.Schema
import org.apache.avro.LogicalType
import org.apache.avro.util.Utf8
import org.apache.parquet.schema.MessageType
import org.apache.hadoop.fs.Path

import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.*
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.logging.log4j.LogManager
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.concurrent.thread

class RiskPublisherAvro(brokers: String, val topic: String){

    val logger = LogManager.getLogger(RiskPublisher::class.java)

    val producer : KafkaProducer<String, ByteArray>

    var schema : Schema? = null

    init{
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.canonicalName
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.canonicalName
        this.producer = KafkaProducer(props)
    }


    fun publish(file: String, schema: Schema){
        val recordInjection = GenericAvroCodecs.toBinary<GenericRecord>(schema)
        val genericDatumReader = GenericDatumReader<GenericRecord>()
        val dataFileReader = DataFileReader<GenericRecord>(File(file), genericDatumReader)
        var record : GenericRecord? = null
        while(dataFileReader.hasNext()){
            record = dataFileReader.next(record)
            val bytes = recordInjection.apply(record)
            val futureResult = producer.send(ProducerRecord<String, ByteArray>(topic, bytes))
            futureResult.get()
        }
        logger.info("done")
    }
}

class RiskConsumerAvro(brokers: String, val topic: String){
    val logger = LogManager.getLogger(RiskConsumer::class.java)
    val consumer : KafkaConsumer<String, ByteArray>

    init{
        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.canonicalName
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = ByteArrayDeserializer::class.java.canonicalName
        props[GsonDeserializer.CONFIG_VALUE_CLASS] = MarketPrice::class.java.canonicalName
        props[ConsumerConfig.GROUP_ID_CONFIG] = "risk-consumer"
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"
        this.consumer = KafkaConsumer(props)
    }


    fun subscribe(schema: Schema){
        val recordInjection = GenericAvroCodecs.toBinary<GenericRecord>(schema)
        consumer.subscribe(listOf(topic))
        while (true) {
            val consumerRecords = consumer.poll(100)

            if (consumerRecords.count() == 0) {
                //logger.info("...")
                continue
            }
            consumerRecords.forEach{p ->
                val record = recordInjection.invert(p.value())!!
                logger.info("received, {}", record)
            }
        }
        // consumer.close()
    }
}
//
//fun getSchema(file: String) : Schema = DataFileReader<GenericRecord>(File(file),  GenericDatumReader<GenericRecord>()).next().schema
//
//
//fun byteArrayToDatum(schema: Schema, byteArray: ByteArray) : GenericRecord{
//    val reader = GenericDatumReader<GenericRecord>(schema)
//    ByteArrayInputStream(byteArray).use {
//        val decoder = DecoderFactory.get().binaryDecoder(it, null)
//        return reader.read(null, decoder)
//    }
//}
//
//fun datumToByteArray(schema: Schema, record: GenericRecord) : ByteArray {
//    val writer = GenericDatumWriter<GenericRecord>(schema)
//    ByteArrayOutputStream().use {
//        val e = EncoderFactory.get().binaryEncoder(it, null)
//        writer.write(record, e)
//        e.flush()
//        return it.toByteArray()
////    }
//}



fun main(args: Array<String>) {
    val publisher = RiskPublisherAvro("localhost:29092", "risk")
    val consumer = RiskConsumerAvro("localhost:29092", "risk")
    val file = "/Users/alex/dev/workspaces/poc/reporting-service/server-base/src/main/resources/dashboard/bj.avro/part-00000-20d44e71-7979-4269-a2de-aba0a675125f-c000.avro"

    val schema = getSchema(file)

    thread{
        consumer.subscribe(schema)
    }

    thread{
        publisher.publish(file, schema)
    }


}