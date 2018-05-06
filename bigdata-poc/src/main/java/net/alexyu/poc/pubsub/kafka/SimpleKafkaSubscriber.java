package net.alexyu.poc.pubsub.kafka;

import com.google.common.collect.ImmutableMap;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import net.alexyu.poc.model.avro.MarketData;
import net.alexyu.poc.pubsub.KafkaRecordListener;
import net.alexyu.poc.pubsub.KafkaSubscriber;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SimpleKafkaSubscriber<K, V> implements KafkaSubscriber<K, V>{


    private static Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaSubscriber.class);

    private KafkaConsumer<K, V> consumer;
    private Map<String, String> config;

    public SimpleKafkaSubscriber(Map<String, String> config) {
        setup(config);
    }

    @Override
    public void setup(Map<String, String> config) {
        this.config = config;
        Properties kafkaProps = new Properties();
        kafkaProps.putAll(config);

        this.consumer = new KafkaConsumer<>(kafkaProps);
    }


    @Override
    public void subscribe(Collection<String> topics, KafkaRecordListener<K, V> listener) {
        this.consumer.subscribe(topics);

        while (true) {
            final ConsumerRecords<K, V> records = this.consumer.poll(1000);
            if (records.count() == 0)
                continue;

            listener.onRecord(records);
        }
    }




    public static void onRecord(ConsumerRecords<String, MarketData> records) {
        for (ConsumerRecord<String, MarketData> record : records){
            //MarketData marketData = record.value();
            LOGGER.info("topic={}, partition={}, offset={}, key={}, timestamp={}, value={}, ", record.topic(), record.partition(), record.offset(), record.key(), record.timestamp(), record.value());

        }
    }

    public static void main(String[] args) throws Exception {

        String topic = "marketdata";
        String groupId = "simple-consumer";
        Map<String, String> config = ImmutableMap.<String, String>builder()
                .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
                .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName())
                .put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")
                .put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
                .build();

        SimpleKafkaSubscriber<String, MarketData> subscriber = new SimpleKafkaSubscriber<String, MarketData>(config);

        subscriber.subscribe(Collections.singletonList(topic), SimpleKafkaSubscriber::onRecord);

    }
}
