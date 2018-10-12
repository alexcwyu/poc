package net.alexcwyu.kafka;

import java.util.Collection;
import java.util.Map;

public interface KafkaSubscriber<K, V> {

    void setup(Map<String, String> config);

    void subscribe(Collection<String> topics, KafkaRecordListener<K, V> listener);

}
