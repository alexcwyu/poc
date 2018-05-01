package net.alexyu.poc.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by alex on 3/29/17.
 */
@SpringBootApplication
public class ChatSpringBootServer {

    private static Logger LOG = LoggerFactory.getLogger(ChatSpringBootServer.class);

    public static void main(String[] args) {
        SpringApplication.run(ChatSpringBootServer.class,args);
    }
}
