package net.alexyu.common;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by alexc on 5/4/2017.
 */
public class MemorizerTest {

    public static Function<Integer, Integer> SLOW_FUNC = (input) -> {
        try {
            Thread.sleep(1000);
        }
        catch(Exception ignored){}
        return input * input;
    };

    @Test
    public void test_memorizer(){
        Memorizer<Integer, Integer> memorizer = new Memorizer(SLOW_FUNC);

        long s1 = System.currentTimeMillis();
        Integer result1 = memorizer.apply(10);
        long s2 = System.currentTimeMillis();


        long s3 = System.currentTimeMillis();
        Integer result2 = memorizer.apply(10);
        long s4 = System.currentTimeMillis();

        assertEquals(result2, result1);
        assertTrue((s2 - s1) >= 999);
        assertTrue((s2 - s1) <= 1010);
        assertTrue((s4 - s3) <=10);
    }
}
