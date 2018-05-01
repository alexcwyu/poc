package net.alexyu.poc.chat.service;

import io.grpc.stub.StreamObserver;
import net.alexyu.poc.Chat;
import net.alexyu.poc.ChatServiceGrpc;
import net.alexyu.poc.greet.LogInterceptor;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

/**
 * Created by alex on 3/30/17.
 */
@Component
@GRpcService(interceptors = {LogInterceptor.class})
public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {
    private static Logger LOG = LoggerFactory.getLogger(ChatService.class);

    private static LinkedHashSet<StreamObserver<Chat.ChatMessageFromServer>> observers = new LinkedHashSet<>();


    @Override
    public StreamObserver<Chat.ChatMessage> chat(StreamObserver<Chat.ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);

        return new StreamObserver<Chat.ChatMessage>() {
            @Override
            public void onNext(Chat.ChatMessage chatMessage) {

                LOG.info("Received message from user = {}, message = {}", chatMessage.getName(), chatMessage.getMessage());

                Chat.ChatMessageFromServer serverMessage = Chat.ChatMessageFromServer.newBuilder().setName(chatMessage.getName()).setMessage(chatMessage.getMessage()).build();
                observers.forEach(observer -> observer.onNext(serverMessage));
            }

            @Override
            public void onError(Throwable t) {
                observers.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }
}
