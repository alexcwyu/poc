package net.alexcwyu.flink


import com.google.common.collect.Lists
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import net.alexyu.grpc.gen.*
import java.io.IOException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Server that manages startup/shutdown of a `Report` server.
 *
 * Note: this file was automatically converted from Java
 */
class KotlinReportServer {

    private var server: Server? = null

    @Throws(IOException::class)
    private fun start() {
        /* The port on which the server should run */
        val port = 50051
        server = ServerBuilder.forPort(port)
                .addService(ReportServiceImpl())
                .build()
                .start()
        logger.log(Level.INFO, "Server started, listening on {0}", port)
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down")
                this@KotlinReportServer.stop()
                System.err.println("*** server shut down")
            }
        })
    }

    private fun stop() {
        server?.shutdown()
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    @Throws(InterruptedException::class)
    private fun blockUntilShutdown() {
        server?.awaitTermination()
    }

    internal class ReportServiceImpl : ReportServiceGrpc.ReportServiceImplBase() {

        var r = Random()

        override fun query(request: ReportRequest, responseObserver: StreamObserver<ReportResponse>) {

            val builder = ReportResponse.newBuilder().setRequestId(request.requestId)

            val count = r.nextInt(10) + 10

            val recordList = Lists.newArrayList<Record>()
            for (i in 0 until count) {
                val record = Record.newBuilder()
                for (column in request.columnsList) {
                    record.putFields(column, Value.newBuilder().setDoubleValue(r.nextInt(10) + r.nextDouble()).build())
                }
                recordList.add(record.build())
            }
            builder.addAllRecords(recordList)
            responseObserver.onNext(builder.build())
            responseObserver.onCompleted()
        }

        override fun subscribe(responseObserver: StreamObserver<ReportResponse>?): StreamObserver<ReportRequest> {
            return super.subscribe(responseObserver)
        }
    }

    companion object {
        private val logger = Logger.getLogger(KotlinReportServer::class.java.name)

        /**
         * Main launches the server from the command line.
         */
        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val server = KotlinReportServer()
            server.start()
            server.blockUntilShutdown()
        }
    }
}