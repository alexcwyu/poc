package net.alexyu.poc.pubsub;

import java.util.Map;

public interface KafkaPublisher<K, V> {

    void setup(Map<String, String> config);

    void publish(String topic, K key, V value);

}
