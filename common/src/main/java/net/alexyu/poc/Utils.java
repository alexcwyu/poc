package net.alexyu.poc;

import java.util.Random;

/**
 * Created by alex on 3/4/17.
 */
public class Utils {

    private static final Random random = new Random();
    private static final boolean isRandom = true;


    public static void delay(long l) {
        if (isRandom) {
            randomDelay(l);
        } else {
            try {
                Thread.sleep(l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
        }
    }

    public static void randomDelay(long l) {
        long delay = l + random.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
