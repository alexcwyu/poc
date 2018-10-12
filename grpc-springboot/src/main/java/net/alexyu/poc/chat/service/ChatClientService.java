package net.alexyu.poc.chat.service;

import com.google.common.collect.Lists;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.alexyu.poc.Chat;
import net.alexyu.poc.ChatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 3/30/17.
 */
@Component
public class ChatClientService {
    private static Logger LOG = LoggerFactory.getLogger(ChatClientService.class);

    private List<Chat.ChatMessageFromServer> messages = Lists.newArrayList();
    private final String host;
    private final int port;
    private final String name;


    private ManagedChannel channel;
    private StreamObserver<net.alexyu.poc.Chat.ChatMessage> chatChannel;

    public ChatClientService(@Value("${grpc.chat.name:Alex}") String name,
                             @Value("${grpc.host:localhost}") String host,
                             @Value("${grpc.port:6565}") int port) {
        this.host = host;
        this.port = port;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();

        this.name = name;
    }

    public void start() {
        final ChatServiceGrpc.ChatServiceStub stub = ChatServiceGrpc.newStub(channel);
        chatChannel = stub.chat(
                new StreamObserver<Chat.ChatMessageFromServer>() {
                    @Override
                    public void onNext(Chat.ChatMessageFromServer message) {
                        messages.add(message);
                        System.out.println(message.getName() + ": " + message.getMessage());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                }
        );
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void chat(String message) {
        chatChannel.onNext(Chat.ChatMessage.newBuilder().setName(name).setMessage(message).build());
    }
}
