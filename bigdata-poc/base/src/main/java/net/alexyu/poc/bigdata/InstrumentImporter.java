package net.alexyu.poc.bigdata;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.bigdata.repository.jdbc.AccountRepository;
import net.alexyu.poc.bigdata.repository.jdbc.InstrumentRelationRepository;
import net.alexyu.poc.bigdata.repository.jdbc.InstrumentRepository;
import net.alexyu.poc.model.proto.Instrument;
import net.alexyu.poc.model.proto.InstrumentRelation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstrumentImporter {

    private static Logger LOGGER = LoggerFactory.getLogger(InstrumentImporter.class);


    public static Map<String, String> COUNTRY_CCY_MAP = new ImmutableMap.Builder()
            .put("Argentina", "ARS")
            .put("Australia", "AUD")
            .put("Austria", "EUR")
            .put("Belgium", "EUR")
            .put("Brazil", "BRL")
            .put("Canada", "CAD")
            .put("China", "CNY")
            .put("Denmark", "DKK")
            .put("Estonia", "EEK")
            .put("Finland", "EUR")
            .put("France", "EUR")
            .put("Germany", "EUR")
            .put("Greece", "EUR")
            .put("Hong Kong", "HKD")
            .put("Iceland", "ISK")
            .put("India", "INR")
            .put("Indonesia", "IDR")
            .put("Ireland", "EUR")
            .put("Israel", "ILS")
            .put("Italy", "EUR")
            .put("Latvia", "EUR")
            .put("Lithuania", "EUR")
            .put("Malaysia", "MYR")
            .put("Mexico", "MXN")
            .put("Netherlands", "EUR")
            .put("New Zealand", "NZD")
            .put("Norway", "NOK")
            .put("Portugal", "EUR")
            .put("Qatar", "QAR")
            .put("Russia", "RUB")
            .put("Singapore", "SGD")
            .put("South Korea", "KRW")
            .put("Spain", "EUR")
            .put("Sweden", "SEK")
            .put("Switzerland", "CHF")
            .put("Taiwan", "TWD")
            .put("Thailand", "THB")
            .put("Turkey", "TRY")
            .put("United Kingdom", "GBP")
            .put("USA", "USD")
            .put("Venezuela", "VEF")
            .build();

    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        InstrumentRepository instrumentRepository = context.getBean(InstrumentRepository.class);
        InstrumentRelationRepository instrumentRelationRepository = context.getBean(InstrumentRelationRepository.class);

        Map<String, Instrument> instrumentMap = Maps.newHashMap();
        Map<String, List<InstrumentRelation>> instrumentRelationMap = Maps.newHashMap();
        buildStkInstruments("/mnt/data/dev/workspaces/poc/bigdata-poc/src/main/resources/data/Yahoo Ticker Symbols.csv", instrumentMap);
        buildAllEtfInstruments("/mnt/data/dev/workspaces/poc/bigdata-poc/src/main/resources/data/etf", instrumentMap, instrumentRelationMap);

        List<InstrumentRelation> instrumentRelations = instrumentRelationMap.values().stream().flatMap(l -> l.stream()).collect(Collectors.toList());

        LOGGER.info("inserting");

        instrumentRepository.insertBatch(Lists.newArrayList(instrumentMap.values()));
        LOGGER.info("insert Instrument done size = {}", instrumentMap.size());

        instrumentRelationRepository.insertBatch(instrumentRelations);

        LOGGER.info("insert InstrumentRelation done size = {}", instrumentRelations.size());
    }


    public static void buildAllEtfInstruments(String directory, Map<String, Instrument> instruments, Map<String, List<InstrumentRelation>> allInstrumentRelations) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path file : dirStream) {
                try (BufferedReader br = Files.newBufferedReader(file)) {

                    LOGGER.warn("creating .... file={}", file);
                    String line = br.readLine();
                    String[] tokens = line.split(":");
                    String symbol = tokens[0].trim();
                    String name = tokens[1].trim();

                    boolean begin = false;

                    List<InstrumentRelation> instrumentRelations = Lists.newArrayList();

                    while ((line = br.readLine()) != null) {
                        if (begin) {
                            tokens = line.split(",");
                            String constName = tokens[0];
                            String constSymbol = tokens[tokens.length - 2];
                            double constWeight = Double.parseDouble(tokens[tokens.length - 1].replace("%", "")) / 100;

                            instrumentRelations.add(InstrumentRelation.newBuilder().setInstId(symbol).setConstInstId(constSymbol).setWeight(constWeight).build());

                            if (!instruments.containsKey(constSymbol)) {
                                LOGGER.warn("missing constituent .... constSymbol={}", constSymbol);
                                instruments.put(constSymbol, createStk(constSymbol, "US", constName, "USA"));
                            }

                        } else if (line.startsWith("ETFdb.com Category:")) {
                            //category = line.replace("ETFdb.com Category:", "").trim();
                        } else if (line.startsWith("Holding,Symbol,Weighting")) {
                            begin = true;
                        }
                    }

                    LOGGER.warn("creating .... symbol={}, name={}", symbol, name);
                    Instrument instrument = Instrument.newBuilder()
                            .setInstId(symbol)
                            .setSymbol(symbol)
                            .setExchId("US")
                            .setName(name)
                            .setType(Instrument.InstType.ETF)
                            .setCcyId("USD")
                            .setCountryId("USA")
                            .build();

                    instruments.put(symbol, instrument);
                    allInstrumentRelations.put(symbol, instrumentRelations);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static String trim(String string) {
        return (string == null) ? string :
                string.trim();
    }

    private static Instrument createStk(String instId, String exchange, String name, String country) {

        String currency = COUNTRY_CCY_MAP.get(country.trim());

        Instrument.Builder instrument = Instrument.newBuilder()
                .setInstId(trim(instId))
                .setSymbol(trim(instId))
                .setExchId(trim(exchange))
                .setName(trim(name))
                .setType(Instrument.InstType.STK)
                .setCcyId(trim(currency))
                .setCountryId(trim(country));

        return instrument.build();
    }


    private static void buildStkInstruments(String file, Map<String, Instrument> instruments) throws IOException {
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
        for (CSVRecord record : records) {
            //Ticker,Name,Exchange,Category Name,Country
            String ticker = record.get("Ticker");
            String name = record.get("Name");
            String exchange = record.get("Exchange");
            String country = record.get("Country");

            LOGGER.trace("{}, {}, {}, {}, {}", ticker, name, exchange, country);

            if (StringUtils.isNotBlank(ticker)
                    && StringUtils.isNotBlank(name)
                    && StringUtils.isNotBlank(exchange)
                    && StringUtils.isNotBlank(country)) {


                instruments.put(ticker, createStk(ticker, exchange, name, country));

            }
        }

    }
}
