package net.alexyu.poc.pubsub.kafka;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.alexyu.poc.pubsub.KafkaPublisher;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

public class PublishPerfTester<K, V> implements KafkaPublisher<K, V> {


    private static Logger LOGGER = LoggerFactory.getLogger(PublishPerfTester.class);
    private static int DEFAULT_NUM_RUN = 1;
    private static int DEFAULT_NUM_THREAD = 1;
    private static int DEFAULT_NUM_MSG = 100_000;


    private final PublisherCallBack callBack;

    private Map<String, String> config;
    private KafkaProducer producer;
    private int publishCount = 0;
    private long publishDuration = 0;

    public PublishPerfTester(PublisherCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void setup(Map<String, String> config) {
        this.config = config;
        Properties properties = new Properties();
        properties.putAll(config);
        this.producer = new KafkaProducer(properties);
    }

    @Override
    public void publish(String topic, K key, V value) {
        long beginTime = System.currentTimeMillis();
        ProducerRecord<K, V> record =
                new ProducerRecord<>(topic, null, beginTime, key,
                        value);

        try {
            Future<RecordMetadata> result = producer.send(record, callBack);
            long publishDuration = System.currentTimeMillis() - beginTime;

            this.publishDuration += publishDuration;
            this.publishCount++;

        } catch (Exception e) {
            LOGGER.warn("fail to publish topic {}, key {}, value {}", topic, key, value, e);
        }
    }


    public static class PublisherCallBack implements Callback {

        private int roundTripCount = 0;

        private final int numOfMsg;
        private final CountDownLatch countDownLatch;

        PublisherCallBack(int numOfMsg, CountDownLatch countDownLatch) {
            this.numOfMsg = numOfMsg;
            this.countDownLatch = countDownLatch;
        }


        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {

            roundTripCount++;

            if (exception != null) {
                LOGGER.warn("fail to publish {}", metadata, exception);
            }
            if (numOfMsg == roundTripCount) {
                countDownLatch.countDown();
            }

        }
    }

    public static class PerformanceTester implements Callable<Pair<Long, Long>> {

        PublishPerfTester<String, String> publisher;
        PublisherCallBack callBack;
        private final int numOfMsg;
        private final String id;
        private final String topic;
        private final Map<String, String> config;
        private final CountDownLatch startCountDownLatch;


        public PerformanceTester(String id, String topic, int numOfMsg, Map<String, String> config, CountDownLatch startCountDownLatch) {
            this.id = id;
            this.topic = topic;
            this.numOfMsg = numOfMsg;
            this.config = config;
            this.startCountDownLatch = startCountDownLatch;
        }


        @Override
        public Pair<Long, Long> call() throws Exception {
            CountDownLatch completeCountDownLatch = new CountDownLatch(1);
            callBack = new PublisherCallBack(numOfMsg, completeCountDownLatch);
            publisher = new PublishPerfTester<>(callBack);
            publisher.setup(config);

            startCountDownLatch.await();

            long beginTime = System.currentTimeMillis();
            for (int i = 0; i < numOfMsg; i++) {
                String message = this.id + ":" + i;
                publisher.publish(topic, id, message);
            }

            completeCountDownLatch.await();

            long duration = System.currentTimeMillis() - beginTime;
            assert publisher.publishCount == numOfMsg;
            assert callBack.roundTripCount == numOfMsg;

            return Pair.of(publisher.publishDuration, duration);
        }
    }

    public static void test(int numRun, int numThread, int numOfMsg, String topic, Map<String, String> config) throws Exception {

        ExecutorService executors = Executors.newFixedThreadPool(numThread);
        for (int run = 1; run <= numRun; run++) {

            System.gc();

            LOGGER.info("Begin: {}/{}", run, numRun);

            CountDownLatch startCountDownLatch = new CountDownLatch(1);
            List<Future<Pair<Long, Long>>> futures = Lists.newArrayList();
            List<Pair<Long, Long>> results = Lists.newArrayList();

            for (int t = 1; t <= numThread; t++) {
                String id = run + ":" + t;
                futures.add(executors.submit(new PerformanceTester(id, topic, numOfMsg, config, startCountDownLatch)));
            }

            long beginTime = System.currentTimeMillis();
            startCountDownLatch.countDown();


            long allBatchPublishDuration = 0;
            long allBatchRoundTripDuration = 0;
            long maxPublishDuration = 0;
            long maxRoundTripDuration = 0;

            for (Future<Pair<Long, Long>> future : futures) {
                Pair<Long, Long> result = future.get();
                results.add(result);

                long publishDurationBatch = result.getLeft();
                long roundTripDuration = result.getRight();
                allBatchPublishDuration += publishDurationBatch;
                allBatchRoundTripDuration += roundTripDuration;
                if (publishDurationBatch > maxPublishDuration) {
                    maxPublishDuration = publishDurationBatch;
                }

                if (roundTripDuration > maxRoundTripDuration) {
                    maxRoundTripDuration = roundTripDuration;
                }

            }

            long duration = System.currentTimeMillis() - beginTime;
            long avgPublishDuration = allBatchPublishDuration / numThread;
            long avgRoundTripDuration = allBatchRoundTripDuration / numThread;


            LOGGER.info("Completed: {}/{}, duration={}, avgPublishDuration={}, avgRoundTripDuration={}",
                    run, numRun, duration, avgPublishDuration, avgRoundTripDuration);
        }
        executors.shutdownNow();
    }


    public static void main(String[] args) throws Exception {
        Map<String, String> config = ImmutableMap.<String, String>builder()
                .put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
                .put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
                .put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer").build();

        test(DEFAULT_NUM_RUN, DEFAULT_NUM_THREAD, DEFAULT_NUM_MSG, "test", config);
    }
}
