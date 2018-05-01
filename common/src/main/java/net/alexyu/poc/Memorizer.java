package net.alexyu.poc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by alex on 3/4/17.
 */
public class Memorizer<Argument, ReturnValue> implements Computable<Argument, ReturnValue> {

    private final ConcurrentHashMap<Argument, FutureTask<ReturnValue>> cache = new ConcurrentHashMap<>();

    private final Computable<Argument, ReturnValue> delegate;

    public Memorizer(Computable<Argument, ReturnValue> delegate) {
        this.delegate = delegate;
    }

    @Override
    public ReturnValue compute(Argument arg) throws InterruptedException, ExecutionException {
        FutureTask<ReturnValue> task = cache.get(arg);
        if (task == null) {
            FutureTask<ReturnValue> newTask = new FutureTask<>(() -> delegate.compute(arg));

            task = cache.putIfAbsent(arg, newTask);

            if (task == null) {
                task = newTask;
                task.run();
            }
        }

        return task.get();
    }
}
