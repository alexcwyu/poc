package net.alexcwyu.grpc.kotlin



import com.google.common.collect.Lists
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import net.alexyu.grpc.gen.ReportRequest
import net.alexyu.grpc.gen.ReportResponse
import net.alexyu.grpc.gen.ReportServiceGrpc
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

/**
 * A simple client that requests a greeting from the [HelloWorldServer].
 */
class KotlinReportClient
/** Construct client for accessing RouteGuide server using the existing channel.  */
internal constructor(private val channel: ManagedChannel) {
    private val blockingStub: ReportServiceGrpc.ReportServiceBlockingStub
            = ReportServiceGrpc.newBlockingStub(channel)

    /** Construct client connecting to HelloWorld server at `host:port`.  */
    constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .build()) {
    }


    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    /** Say hello to server.  */
    fun query(id: String) {
        logger.log(Level.INFO, "Will try to greet {0}...", id)
        val request = ReportRequest.newBuilder().setRequestId(id)
                .addAllColumns(Lists.newArrayList<String>("delta", "gamma", "tv", "rho"))
                .addAllGroupBy(Lists.newArrayList<String>("underlying")).build()
        val response: ReportResponse =  try {
            blockingStub.query(request)
        } catch (e: StatusRuntimeException) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.status)
            return
        }

        logger.info("response: ${response}")
    }

    companion object {
        private val logger = Logger.getLogger(KotlinReportClient::class.java.name)

        /**
         * Greet server. If provided, the first element of `args` is the name to use in the
         * greeting.
         */
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val client = KotlinReportClient("localhost", 50051)
            try {
                client.query("test")
            } finally {
                client.shutdown()
            }
        }
    }
}