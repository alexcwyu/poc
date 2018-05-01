package net.alexyu.poc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by alex on 3/4/17.
 */
public class App {

    private final static Executor executor = Executors.newFixedThreadPool(400);

    private final static int COUNT = Runtime.getRuntime().availableProcessors();
    //private final static int COUNT = 8;


    private final static List<Shop> shops = IntStream.range(1, COUNT * 2).mapToObj(i -> new Shop("test_" + i, executor)).collect(toList());

    private final static DiscountService discountService = new DiscountService();
    private final static ExchangeService exchangeService = new ExchangeService();


    public static void timeIt(String name, BiFunction<List<Shop>, String, List<Quote>> function, final List<Shop> shops, final String product) {
        long start = System.currentTimeMillis();
        List<Quote> quotes = function.apply(shops, product);

        long completed = System.currentTimeMillis();
        System.out.println("name=" + name + ", time=" + (completed - start));
    }

    public static List<Quote> findPriceSync(final List<Shop> shops, final String product) {
        return shops.stream()
                .map(shop -> Quote.newQuote(shop.name, product, shop.getPrice(product)))
                .map(quote -> {
                    double rate = exchangeService.getRate("EUR", "USD");
                    return exchangeService.convertToUSD(quote, rate);
                })
                .map(discountService::getAndApplyDiscount).collect(toList());

    }

    public static List<Quote> findPriceParallelStream(final List<Shop> shops, final String product) {
        return shops.parallelStream()
                .map(shop -> Quote.newQuote(shop.name, product, shop.getPrice(product)))
                .map(quote -> {
                    double rate = exchangeService.getRate("EUR", "USD");
                    return exchangeService.convertToUSD(quote, rate);
                })
                .map(discountService::getAndApplyDiscount).collect(toList());
    }

    public static List<Quote> findPriceCompletableFutures(final List<Shop> shops, final String product) {
        List<Quote> result = new ArrayList<>();
        CompletableFuture[] futures =
                shops.stream().map(shop ->
                        CompletableFuture.supplyAsync(() -> Quote.newQuote(shop.name, product, shop.getPrice(product)), executor)
                                .thenCombine(CompletableFuture.supplyAsync(() -> exchangeService.getRate("EUR", "USD")), (quote, rate) -> exchangeService.convertToUSD(quote, rate))
                                .thenApply(q -> {System.out.println(q); return q;})
                                .thenCompose(quote -> CompletableFuture.supplyAsync(() -> discountService.getAndApplyDiscount(quote), executor))
                                .thenAccept(q -> {
                                    result.add(q);
                                    System.out.println(q);
                                })
                ).toArray(size -> new CompletableFuture[size]);
        CompletableFuture.allOf(futures).join();


        return result;
    }

    public static void main(String[] args) {


        final String product = "iphone8";

//        timeIt("Sync", App::findPriceSync, shops, product);
        timeIt("ParallelStream", App::findPriceParallelStream, shops, product);
        timeIt("CompletableFutures", App::findPriceCompletableFutures, shops, product);
    }
}
