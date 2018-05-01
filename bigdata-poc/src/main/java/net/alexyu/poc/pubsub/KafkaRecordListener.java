package net.alexyu.poc.pubsub;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface KafkaRecordListener<K, V> {

    void onRecord(ConsumerRecords<K, V> records );
}
