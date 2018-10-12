package net.alexyu.poc.bigdata;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.bigdata.repository.jdbc.AccountRepository;
import net.alexyu.poc.bigdata.repository.jdbc.InstrumentRepository;
import net.alexyu.poc.bigdata.repository.jdbc.PositionRepository;
import net.alexyu.poc.model.proto.Account;
import net.alexyu.poc.model.proto.Instrument;
import net.alexyu.poc.model.proto.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PositionGenerator {

    private static Logger LOGGER = LoggerFactory.getLogger(PositionGenerator.class);


    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);

        AccountRepository accountRepository = context.getBean(AccountRepository.class);
        InstrumentRepository instrumentRepository = context.getBean(InstrumentRepository.class);
        PositionRepository positionRepository = context.getBean(PositionRepository.class);

        List<String> acctIds = accountRepository.findAll().stream().map(Account::getAcctId).collect(Collectors.toList());
        List<String> instIds = instrumentRepository.findAll().stream().map(Instrument::getInstId).collect(Collectors.toList());
        Random rand = new Random();

        long timestamp = System.currentTimeMillis();


        Map<String, Double> prices = instIds.stream().collect(Collectors.toMap((instId) -> instId, (instId) -> ((double) rand.nextInt(100000)) / 100));


        AtomicLong counter = new AtomicLong(0);

        acctIds.stream().forEach(acctId -> {
            LOGGER.info("start {}", acctId);
            int numOfPosition = rand.nextInt(2000) +100;
            Set<String> posInst = Sets.newHashSet();
            List<Position> positions = Lists.newArrayList();
            for (int i = 0; i < numOfPosition; i++) {
                int retry = 0;
                while (retry < 100) {
                    retry++;
                    String instId = instIds.get(rand.nextInt(instIds.size()));

                    if (posInst.contains(instId)) {
                        continue;
                    } else {
                        posInst.add(instId);

                        int qty = (rand.nextInt(2000000) - 1000000 / 100 * 100);

                        double price = prices.get(instId);
                        double randomFactor =  1- (rand.nextDouble() * 20 -10) / 100;
                        double avgPrice = price * randomFactor;
                        Position position = Position.newBuilder().
                                setAcctId(acctId)
                                .setInstId(instId)
                                .setTotalQty(qty)
                                .setAvgPrice(avgPrice)
                                .setCreateTimestamp(timestamp)
                                .setUpdateTimestamp(timestamp).build();

                        positions.add(position);
                        break;
                    }
                }
            }
            Set<String> instIdVerify = positions.stream().map(Position::getInstId).collect(Collectors.toSet());
            LOGGER.info("saving {}, size {} instIdSize={}", acctId, positions.size(), instIdVerify.size());
            counter.addAndGet(positions.size());
            positionRepository.insertBatch(positions);
            LOGGER.info("done {}", acctId);
        });


        LOGGER.info("done");
    }
}
