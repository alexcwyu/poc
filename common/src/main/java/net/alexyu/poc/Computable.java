package net.alexyu.poc;

import java.util.concurrent.ExecutionException;

/**
 * Created by alex on 3/4/17.
 */
public interface Computable<Argument, ReturnValue> {
    ReturnValue compute(Argument arg) throws InterruptedException, ExecutionException;
}