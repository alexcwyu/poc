package net.alexyu.poc.chat;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.alexyu.poc.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by alex on 3/30/17.
 */
public class ChatServer {

    private static Logger LOG = LoggerFactory.getLogger(ChatServer.class);

    private Server server;

    private final int port;

    public ChatServer(int port) {
        this.port = port;
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(new ChatService()).build().start();

        LOG.info("Server started, listened on port {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("*** shutting down gRPC server since JVM is shutting down");
            ChatServer.this.stop();
            LOG.info("*** server shut down");
        }));
    }


    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final ChatServer server = new ChatServer(6565);
        server.start();
        server.blockUntilShutdown();

//        Server server = ServerBuilder.forPort(6565).addService(new ChatService()).build().start();
//        server.awaitTermination();
    }
}
