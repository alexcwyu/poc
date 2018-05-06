package net.alexyu.poc.pubsub.kafka;

import com.google.common.collect.ImmutableMap;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import net.alexyu.poc.model.avro.MarketData;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import net.alexyu.poc.pubsub.KafkaPublisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class SimpleKafkaPublisher<K, V> implements KafkaPublisher<K, V> {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaPublisher.class);

    private KafkaProducer<K, V> producer;
    private Map<String, String> config;

    public SimpleKafkaPublisher(Map<String, String> config) {
        setup(config);
    }

    @Override
    public void setup(Map<String, String> config) {
        this.config = config;
        Properties kafkaProps = new Properties();
        kafkaProps.putAll(config);

        this.producer = new KafkaProducer<>(kafkaProps);
    }

    @Override
    public void publish(String topic, K key, V value) {
        ProducerRecord<K, V> record =
                new ProducerRecord<>(topic, key,
                        value);
        try {
            this.producer.send(record);
        } catch (Exception e) {
            LOGGER.error("fail to publish {}, {}, {}", topic, key, value, e);
        }
    }


    public static void main(String[] args) throws Exception {

        String topic = "marketdata";
        String clientId = "simple-producer1";
        Map<String, String> config = ImmutableMap.<String, String>builder()
                .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                .put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName())
                .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName())
                .put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")
                .put("acks", "1")
                .put("compression.type", "snappy")
                .put("retries", "10")
                .put("client.id", clientId)
                //.put("batch.size", "1000")
                //.put("linger.ms", "10")
                .build();

        SimpleKafkaPublisher<String, MarketData> publisher = new SimpleKafkaPublisher<>(config);


        Random random = new Random();
        while (true) {
            MarketData marketData = MarketDataGenerator.next();
            publisher.publish(topic, marketData.getInstid(), marketData);

            LOGGER.info("publishing {}", marketData);
            Thread.sleep(random.nextInt(10000));
        }

    }
}
