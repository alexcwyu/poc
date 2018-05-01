package net.alexyu.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

/**
 * Created by alexc on 5/4/2017.
 */
public class Memorizer<T, R> implements Function<T, R> {

    private final Map<T, Future<R>> cache = new ConcurrentHashMap<T, Future<R>>();
    private final Function<T, R> function;

    public Memorizer(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public R apply(T input) {
        Future<R> future = cache.get(input);
        if (future == null) {
            FutureTask<R> task = new FutureTask<>(() -> function.apply(input));
            future = cache.putIfAbsent(input, task);
            if (future == null){
                future = task;
                task.run();
            }
        }
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}