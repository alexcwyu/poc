package net.alexyu.poc.greet.service;

import io.grpc.stub.StreamObserver;
import net.alexyu.poc.GreeterGrpc;
import net.alexyu.poc.GreeterOuterClass;
import net.alexyu.poc.greet.LogInterceptor;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by alex on 3/30/17.
 */
@Component
@GRpcService(interceptors = {LogInterceptor.class})
public class GreeterService extends GreeterGrpc.GreeterImplBase {
    private static Logger LOG = LoggerFactory.getLogger(GreeterService.class);

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        LOG.info("Getting message from {}", request.getName());
        final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder().setMessage("Hello " + request.getName());
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }
}
