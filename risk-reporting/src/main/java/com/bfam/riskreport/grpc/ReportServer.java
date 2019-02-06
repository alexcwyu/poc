package com.bfam.riskreport.grpc;

import com.bfam.riskreport.model.Record;
import com.bfam.riskreport.model.Row;
import com.bfam.riskreport.model.Value;
import com.bfam.riskreport.service.ReportRequest;
import com.bfam.riskreport.service.ReportResponse;
import com.bfam.riskreport.service.ReportServiceGrpc;
import com.google.common.collect.Lists;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class ReportServer {
    private static final Logger logger = Logger.getLogger(ReportServer.class.getName());

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new ReportServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                ReportServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final ReportServer server = new ReportServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {
        Random r = new Random();

        @Override
        public void query(ReportRequest request, StreamObserver<ReportResponse> responseObserver) {

            ReportResponse.Builder builder = ReportResponse.newBuilder().setRequestId(request.getRequestId());

            int count = r.nextInt(10) + 10;

            List<Row> recordList = Lists.newArrayList();
            for (int i = 0; i < count; i++) {
                Row.Builder record = Row.newBuilder();
                for (String column : request.getColumnsList()) {
                    record.addValues(Value.newBuilder().setDoubleValue(r.nextInt(10) + r.nextDouble()).build());
                }
                recordList.add(record.build());
            }
            builder.addAllRows(recordList);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }

        //https://github.com/grpc/grpc-java/blob/master/examples/src/main/java/io/grpc/examples/manualflowcontrol/ManualFlowControlServer.java
        @Override
        public StreamObserver<ReportRequest> subscribe(StreamObserver<ReportResponse> responseObserver) {
            return super.subscribe(responseObserver);
        }
    }
}