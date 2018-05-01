package net.alexyu.poc;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * Created by alex on 3/4/17.
 */
public class Shop {

    private Random random = new Random();
    public final String name;
    private final Executor executor;

    public Shop(String name, Executor executor) {
        this.name = name;
        this.executor = executor;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(final String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product), executor);
    }


    private double calculatePrice(String product) {
        Utils.delay(1000l);
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
