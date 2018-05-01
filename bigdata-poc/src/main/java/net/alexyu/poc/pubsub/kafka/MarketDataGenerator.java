package net.alexyu.poc.pubsub.kafka;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.alexyu.poc.model.MarketData;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MarketDataGenerator {

    private static Map<String, Double> instIdPrices = Maps.newHashMap(ImmutableMap.<String, Double>builder()
            .put("msft", 95.0)
            .put("fb", 166.28)
            .put("goog", 1077.33)
            .put("spx", 2670.14)
            .put("vix", 16.88)
            .put("amd", 9.99)
            .put("ibm", 144.9)
            .put("intel", 51.53)
            .put("btc", 8797.73)
            .put("eth", 609.0).build());


    private static List<String> instIds = Lists.newArrayList(instIdPrices.keySet());

    private static Random generator = new Random();

    public static MarketData next() {

        String instId = instIds.get(generator.nextInt(instIds.size()));

        double price = instIdPrices.get(instId) + ((generator.nextDouble() - 0.5) / 1000);
        instIdPrices.put(instId, price);

        return new MarketData(instId, "lastPrice", System.currentTimeMillis(), price);
    }


}
