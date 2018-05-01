package net.alexyu.poc.greet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by alex on 3/29/17.
 */
@SpringBootApplication
public class GreeterSpringBootServer {

    private static Logger LOG = LoggerFactory.getLogger(GreeterSpringBootServer.class);

    public static void main(String[] args) {
        SpringApplication.run(GreeterSpringBootServer.class,args);
    }
}
