package net.alexyu.poc.greet;

import net.alexyu.poc.greet.service.GreeterConsumer;

/**
 * Created by alex on 3/30/17.
 */
public class GreeterConsoleClient {

    public static void main(String[] args) throws Exception {
        GreeterConsumer client = new GreeterConsumer("localhost", 6565);
        try {
            String user = "Alex";
            if (args.length > 0) {
            }
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }
}
