package net.alexyu.poc.greet;

import net.alexyu.poc.greet.service.GreeterConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by alex on 3/30/17.
 */
//@ComponentScan
//@EnableAutoConfiguration
@SpringBootApplication
public class GreeterSpringBootClient {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GreeterSpringBootClient.class, args);


        context.getBean(GreeterConsumer.class).greet("Alex"); // <-- he
    }

}
