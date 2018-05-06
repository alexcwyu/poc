package net.alexyu.poc.bigdata;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.alexyu.poc.model.proto.Basket;
import net.alexyu.poc.model.proto.Constituent;
import net.alexyu.poc.model.proto.Instrument;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

public class SymbolReader {

    private static Logger LOGGER = LoggerFactory.getLogger(SymbolReader.class);


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

        Map<String, Instrument> instruments = Maps.newHashMap();

        buildStkInstruments("/mnt/data/dev/workspaces/poc/bigdata-poc/src/main/resources/data/Yahoo Ticker Symbols.csv", instruments);

        buildAllEtfInstruments("/mnt/data/dev/workspaces/poc/bigdata-poc/src/main/resources/data/etf", instruments);
        //buildEtfInstruments("/mnt/data/dev/workspaces/poc/bigdata-poc/src/main/resources/data/SPY-holdings.csv", instruments);
    }


    public static void buildAllEtfInstruments(String directory, Map<String, Instrument> instruments) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path file : dirStream) {
                buildEtfInstruments(file, instruments);
            }
        } catch (DirectoryIteratorException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void buildEtfInstruments(Path file, Map<String, Instrument> instruments) throws IOException {


        try (BufferedReader br = Files.newBufferedReader(file)) {

            LOGGER.warn("creating .... file={}", file);
            String line = br.readLine();
            String[] tokens = line.split(":");
            String symbol = tokens[0].trim();
            String name = tokens[1].trim();
            String category = null;

            boolean begin = false;

            List<Constituent> constituents = Lists.newArrayList();

            while ((line = br.readLine()) != null) {
                if (begin) {
                    tokens = line.split(",");
                    String constName = tokens[0];
                    String constSymbol = tokens[tokens.length-2];
                    double constWeight = Double.parseDouble(tokens[tokens.length-1].replace("%", "")) / 100;

                    constituents.add(Constituent.newBuilder().setInstId(constSymbol).setWeight(constWeight).build());

                    if (!instruments.containsKey(constSymbol)) {
                        LOGGER.warn("missing constituent .... constSymbol={}", constSymbol);
                        instruments.put(constSymbol, createStk(constSymbol, "US", constName, "USA", null));
                    }

                } else if (line.startsWith("ETFdb.com Category:")) {
                    category = line.replace("ETFdb.com Category:", "").trim();
                } else if (line.startsWith("Holding,Symbol,Weighting")) {
                    begin = true;
                }


            }

            LOGGER.warn("creating .... symbol={}, name={}", symbol, name);
            Instrument instrument = Instrument.newBuilder()
                    .setInstId(symbol)
                    .setExchId("US")
                    .setName(name)
                    .setType(Instrument.InstType.ETF)
                    .setCategory(category)
                    .setCcyId("USD")
                    .setBasket(Basket.newBuilder()
                            .addAllConstituents(constituents)
                            .setInstId(symbol)
                            .setType(Basket.BasketType.SUM))
                    .build();

            instruments.put(symbol, instrument);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String trim(String string) {
        return (string == null) ? string :
                string.trim();
    }

    private static Instrument createStk(String instId, String exchange, String name, String country, String category) {

        String currency = COUNTRY_CCY_MAP.get(country.trim());

        Instrument.Builder instrument = Instrument.newBuilder()
                .setInstId(trim(instId))
                .setExchId(trim(exchange))
                .setName(trim(name))
                .setType(Instrument.InstType.STK)
                .setCountry(trim(country))
                .setCcyId(trim(currency));

        if (category != null) {
            instrument.setCategory(trim(category));
        }
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
            String category = record.get("Category Name");
            String country = record.get("Country");

            LOGGER.trace("{}, {}, {}, {}, {}", ticker, name, exchange, category, country);

            if (StringUtils.isNotBlank(ticker)
                    && StringUtils.isNotBlank(name)
                    && StringUtils.isNotBlank(exchange)
                    && StringUtils.isNotBlank(country)) {


                instruments.put(ticker, createStk(ticker, exchange, name, country, category));

//                if (instrumentMap.containsKey(instrument.getInstId())) {
//                    LOGGER.warn("replacing duplicated .... existing={}, new={}", instrumentMap.get(instrument.getInstId()), instruments);
//                }
//                instrumentMap.put(instrument.getInstId(), instrument);
            }
        }

    }
}
