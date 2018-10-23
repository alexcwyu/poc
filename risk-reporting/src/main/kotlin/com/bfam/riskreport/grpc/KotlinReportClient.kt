package com.bfam.riskreport.grpc



import com.google.common.collect.Lists
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import com.bfam.riskreport.service.ReportRequest
import com.bfam.riskreport.service.ReportResponse
import com.bfam.riskreport.service.ReportServiceGrpc
import io.grpc.netty.NettyChannelBuilder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * A simple client that requests a greeting from the [HelloWorldServer].
 */
class KotlinReportClient
/** Construct client for accessing RouteGuide server using the existing channel.  */
internal constructor(private val channel: ManagedChannel) {
    private val blockingStub: ReportServiceGrpc.ReportServiceBlockingStub
            = ReportServiceGrpc.newBlockingStub(channel)

    /** Construct client connecting to HelloWorld server at `host:port`.  */
    constructor(host: String, port: Int) : this(NettyChannelBuilder.forAddress(host, port)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .maxInboundMessageSize(999999999)
            .usePlaintext()
            .build()) {
    }


    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    /** Say hello to server.  */
    fun query(id: String) {
        val request = ReportRequest.newBuilder().setRequestId(id)
                //.addAllColumns(Lists.newArrayList<String>("delta", "gamma", "tv", "rho"))
                //.addAllGroupBy(Lists.newArrayList<String>("underlying"))
                .build()

        var response: ReportResponse? = null
        val executionTime = measureTimeMillis {
            response = try {
                blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).query(request)
            } catch (e: StatusRuntimeException) {
                logger.log(Level.WARNING, "RPC failed: {0}", e.status)
                return
            }
        }
        logger.info("id: {$id}, count: ${response?.rowsCount}, executionTime: ${executionTime}")
        while (true) {
            val cur = max.get()

            if (executionTime >= cur) {
                if (max.compareAndSet(cur, executionTime))
                    break
            } else
                break
        }
        total.addAndGet(executionTime)
    }

    companion object {
        private val logger = Logger.getLogger(KotlinReportClient::class.java.name)

        private var max = AtomicLong(0)
        private var total = AtomicLong(0)

        /**
         * Greet server. If provided, the first element of `args` is the name to use in the
         * greeting.
         */
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {

            val numberOfThreads = 10
            val numQuery = 10
            val latch = CountDownLatch(numberOfThreads)
            for (i in 1..numberOfThreads) {
                thread() {
                    val client = KotlinReportClient("localhost", 50051)
                    try {
                        for (j in 1..numQuery) {
                            val id = "thread ${i} -- ${j}"
                            client.query(id)
                        }
                    } finally {
                        client.shutdown()
                        latch.countDown()
                    }
                }
            }
            latch.await()

            logger.info("max: ${max.get()}, average: ${total.get()/numberOfThreads/numQuery}")
        }
    }
}