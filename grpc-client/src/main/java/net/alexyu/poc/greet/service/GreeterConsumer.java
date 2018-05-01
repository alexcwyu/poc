package net.alexyu.poc.greet.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.alexyu.poc.GreeterGrpc;
import net.alexyu.poc.GreeterOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 3/30/17.
 */
@Component
public class GreeterConsumer {
    private static Logger LOG = LoggerFactory.getLogger(GreeterConsumer.class);

    private final String host;
    private final int port;
    private final ManagedChannel channel;

    public GreeterConsumer(@Value("${grpc.host:localhost}") String host,
                           @Value("${grpc.port:6565}") int port) {
        this.host = host;
        this.port = port;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        final GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
        GreeterOuterClass.HelloReply reply = stub.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build());
        LOG.info("receiving reply : {}", reply.getMessage());

    }
}
