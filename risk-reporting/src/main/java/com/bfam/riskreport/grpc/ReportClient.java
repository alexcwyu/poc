package com.bfam.riskreport.grpc;


import com.google.common.collect.Lists;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import com.bfam.riskreport.service.ReportRequest;
import com.bfam.riskreport.service.ReportResponse;
import com.bfam.riskreport.service.ReportServiceGrpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link ReportServer}.
 */
public class ReportClient {
    private static final Logger logger = Logger.getLogger(ReportClient.class.getName());

    private final ManagedChannel channel;
    private final ReportServiceGrpc.ReportServiceBlockingStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public ReportClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    /** Construct client for accessing HelloWorld server using the existing channel. */
    ReportClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = ReportServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public void query(String id) {
        logger.info("Will try to greet " + id + " ...");
        ReportRequest request = ReportRequest.newBuilder().setRequestId(id)
                .addAllColumns(Lists.newArrayList("delta", "gamma", "tv", "rho"))
                .addAllGroupBy(Lists.newArrayList("underlying")).build();
        ReportResponse response;
        try {
            response = blockingStub.query(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Report: " + response);
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting.
     */
    public static void main(String[] args) throws Exception {
        ReportClient client = new ReportClient("localhost", 50051);
        try {
            client.query("test");
        } finally {
            client.shutdown();
        }
    }
}