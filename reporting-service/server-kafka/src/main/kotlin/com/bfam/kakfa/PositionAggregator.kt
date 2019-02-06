package com.bfam.kakfa

import com.bfam.kakfa.publisher.PricePublisher
import com.bfam.model.MarketPrice
import com.bfam.model.MarketPriceStats
import com.bfam.model.Position
import com.bfam.model.Trade
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import java.util.*
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.logging.log4j.LogManager
import org.apache.kafka.common.serialization.Serdes.WrapperSerde
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import kotlin.concurrent.thread


private val logger = LogManager.getLogger(PricePublisher::class.java)


class TradeSerde : WrapperSerde<Trade>(GsonSerializer<Trade>(), GsonDeserializer(Trade::class.java))

class PositionSerde : WrapperSerde<Position>(GsonSerializer<Position>(), GsonDeserializer(Position::class.java))


fun main(args: Array<String>) {
    val brokers = "localhost:29092"
    val inTopic = "trade"
    val table = "position-table2"
    val outTopic = "position-stream2"
    val props = Properties()

    props[StreamsConfig.APPLICATION_ID_CONFIG]=  "position-1"
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
    props[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = "100"
    //props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG]= Serdes.StringSerde::class.java.canonicalName
    //props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG]= TradeSerde::class.java.canonicalName
    val tradeSerde = TradeSerde()
    val positionSerde = PositionSerde()

    val streamsBuilder = StreamsBuilder()

    val tradeStream: KStream<String, Trade> = streamsBuilder
            .stream<String, Trade>(inTopic, Consumed.with(Serdes.String(), tradeSerde))


    val positionTable = tradeStream
            //.groupByKey()
            .groupBy({key: String?, value: Trade? ->  value?.symbol }, Serialized.with(Serdes.String(), tradeSerde))
            .aggregate(
                    { Position("Symbol", "", "") },
                    { key, value, aggregate ->
                        val longPrice= if (value.qty > 0) (aggregate.avgLongPrice * aggregate.totalLongQty) + (value.price * value.qty) / (aggregate.totalLongQty + value.qty) else aggregate.avgLongPrice
                        val shortPrice= if (value.qty < 0) (aggregate.avgShortPrice * aggregate.totalShortQty) + (value.price * value.qty) / (aggregate.totalShortQty + value.qty) else aggregate.avgShortPrice
                        val longQty = if (value.qty > 0) aggregate.totalLongQty + value.qty else aggregate.totalLongQty
                        val shortQty = if (value.qty < 0) aggregate.totalShortQty + value.qty else aggregate.totalShortQty
                        Position("Symbol", value.symbol, value.symbol, longQty, longPrice, shortQty, shortPrice)
                    }
                    ,Materialized.`as`<String, Position, KeyValueStore<Bytes, ByteArray>>(table).withKeySerde(Serdes.String()).withValueSerde(positionSerde)
            )

    logger.info(positionTable.queryableStoreName())
    val positionStream= positionTable.toStream()


    positionStream.to(outTopic, Produced.with(Serdes.String(), positionSerde))

    val topology = streamsBuilder.build()

    //Thread.currentThread().setContextClassLoader(null);
    val streams = KafkaStreams(topology, props)
    logger.info(topology.describe())

    thread {

        Thread.sleep(5000)
        val keyValueStore : ReadOnlyKeyValueStore<Bytes, ByteArray> = streams.store(table, QueryableStoreTypes.keyValueStore())
        while (true) {
            try {
                logger.info("==========printing stats=======")
                val range = keyValueStore.all()
                while (range.hasNext()) {
                    val next = range.next()
                    logger.info("{} = {}", next.key, next.value)
                }
                range.close()
            }
            catch (e: Exception){
                logger.error("fail....", e)
            }
            Thread.sleep(5000)
        }
    }
    streams.start()

}