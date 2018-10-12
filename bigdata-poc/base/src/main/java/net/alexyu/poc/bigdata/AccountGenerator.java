package net.alexyu.poc.bigdata;

import com.google.common.collect.Lists;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.bigdata.repository.jdbc.AccountRepository;
import net.alexyu.poc.model.proto.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Random;

public class AccountGenerator {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountGenerator.class);
    public static final List<String> FIRMS = Lists.newArrayList(
            "Goldman Sachs",
            "JP Morgan Chase",
            "Barclays",
            "Bank of America",
            "Morgan Stanley",
            "Deutsche Bank",
            "Citigroup",
            "Credit Suisse",
            "UBS",
            "HSBC",
            "Adage",
            "ADG",
            "Alantra",
            "Alcentra",
            "Allianz",
            "Amplitude Intl",
            "Ancient Art",
            "Ansons",
            "AQR",
            "Aspect",
            "Autonomy",
            "Balyasny",
            "BlackRock",
            "BloombergSen",
            "Blue Diamond",
            "Bridgewater Associates",
            "Camden",
            "Cantab",
            "Capital",
            "Capula",
            "Carlson",
            "Chatham",
            "Chenavari",
            "Cheyne",
            "Citadel",
            "Cologny",
            "Connor Clark Lunn",
            "Contrarian",
            "DE Shaw Group",
            "Deer Park Road",
            "Dorsal",
            "Dunn",
            "Element",
            "Fortress Group",
            "Gemsstock",
            "Greylock",
            "GSA",
            "Guggenheims",
            "Harvest Strategies",
            "Hawk Ridge",
            "Hildene",
            "HMI",
            "IPM",
            "ISAM",
            "Knight Vinke",
            "Linden",
            "LMR",
            "Long Pond",
            "Man Group",
            "Mangrove",
            "Marshall Wace",
            "Masters",
            "MIG",
            "Millennium",
            "Moab",
            "Napier Park",
            "PanAgora.",
            "Parametrica",
            "Pharo Global",
            "Pine River",
            "Pinpoint",
            "Plinius",
            "Polar",
            "Polygon",
            "Quantedge",
            "Quest",
            "Ramsey Quantitative Systems",
            "RBC Global",
            "Renaissance Technologies",
            "Sachem Head",
            "Saemor BV",
            "SECOR",
            "Segantii",
            "Select Equity Group",
            "Serengeti",
            "Sherborne",
            "Sound Point",
            "Splendor",
            "Structured Portfolio",
            "TCA Group",
            "TCI",
            "Theleme",
            "Tide Point",
            "Tilden Park",
            "Two Sigmas",
            "Varde",
            "Varden Pacific",
            "Verde",
            "Verition",
            "VR Advisory Services",
            "Waterfall",
            "Welton",
            "Whitebox"
    );

//    public static final List<String> LOCATION = Lists.newArrayList(
//            "Atlanta",
//            "Austin",
//            "Boston",
//            "Cambridge",
//            "Carmel",
//            "Chatham",
//            "Chicago",
//            "Copenhagen",
//            "Dallas",
//            "Greenwich",
//            "Hong Kong",
//            "London",
//            "Los Angeles",
//            "Louisville",
//            "Madrid",
//            "Minneapolis",
//            "Minnetonka",
//            "Monaco",
//            "New York",
//            "Newport Beach",
//            "Old Greenwich",
//            "Paris",
//            "Pfaffikon",
//            "Redwood City",
//            "San Francisco",
//            "Sao Paulo",
//            "Singapore",
//            "Stamford",
//            "Steamboat Springs",
//            "Stockholm",
//            "Stuart",
//            "The Hague",
//            "Toronto",
//            "Vancouver",
//            "Westport",
//            "Zug, Switzerland"
//    );

    public static final List<String> STRATEGY = Lists.newArrayList(
            "Absolute Return",
            "Activist / Event Driven",
            "Asia Multistrategy",
            "Asset-Backed Loans",
            "Asset-Backed Securities",
            "Convertible Arbitrage",
            "Credit",
            "Credit Long Bias",
            "Credit Long / Short",
            "CTA",
            "CTA Long-Term Trend Follower",
            "Distressed",
            "Distressed Emerging Markets",
            "Distressed Securities",
            "Diversified CTA",
            "Diversified Growth",
            "Diversified Systematic",
            "Diversified Systematic Global Macro",
            "Emerging Markets",
            "Emerging Markets Distressed",
            "Equity & Equity Linked Strategies",
            "Equity and Credit Long Bias",
            "Equity Long/Short",
            "Equity Long/Short, Event Driven, Activist",
            "Equity Long-Bias",
            "Equity Long-Only",
            "Equity Long-Only Activist",
            "Equity Market Neutral",
            "European-Backed Securities",
            "European Equity Long / Short",
            "European Small-Cap Activist",
            "Event Driven",
            "Fixed-Income Arbitrage",
            "Fixed-Income Directional",
            "Fixed-Income Relative Value",
            "Global Equity Market Neutral",
            "Global Macro",
            "Macro",
            "Managed Futures",
            "Mortgage-Backed Securities",
            "Multistrategy",
            "Multistrategy / Fixed Income",
            "Opportunistic Credit",
            "Quantitative Equity Long-Bias",
            "Quantitative Global Multistrategy",
            "Quantitative Systematic Trading",
            "Relative Value Arbitrage",
            "Statistical Arbitrage",
            "Stressed / Distressed Credit",
            "Systematic Futures",
            "Systematic Macro",
            "Systematic Short-Term CTA"
    );


    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rnd = new Random();

    private static String padWithRndString(String prefix, int len) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = prefix.length(); i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString().substring(0, len);
    }


    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        AccountRepository accountRepository = context.getBean(AccountRepository.class);

        List<Account.BusGroup> availableBusGroup = Lists.newArrayList(Account.BusGroup.PB, Account.BusGroup.LD, Account.BusGroup.IED);
        List<Account.Region> regions = Lists.newArrayList(Account.Region.NY, Account.Region.LN, Account.Region.HK);
        Random rand = new Random();

        List<Account> accounts = Lists.newArrayList();

        for (String firm : FIRMS) {

            int numOfStrategy = rand.nextInt(STRATEGY.size());
            int regionStartPos = rand.nextInt(regions.size());
            int fundCount = 0;
            for (int i = 0; i < 2; i++) {

                int regionPos = (regionStartPos + i) % regions.size();
                Account.Region region = regions.get(regionPos);

                for (int j = 0; j < numOfStrategy; j++) {
                    fundCount++;
                    String strategy = STRATEGY.get(rand.nextInt(STRATEGY.size()));
                    String acctPrefix = padWithRndString(firm.replaceAll("[^0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]", "").toUpperCase(), 8);

                    String accountId = (fundCount < 10) ? acctPrefix + "0" + fundCount : acctPrefix + fundCount;
                    Account.BusGroup busGroup = availableBusGroup.get(rand.nextInt(availableBusGroup.size()));

                    Account account = Account.newBuilder()
                            .setAcctId(accountId)
                            .setAcctName(firm + ", " + region.name() + ", " + strategy)
                            .setFirmName(firm)
                            .setBusGroup(busGroup)
                            .setRegion(region).build();
                    accounts.add(account);

                    if (fundCount >= 90) {
                        break;
                    }
                }
            }
        }

        LOGGER.info("inserting");

        accountRepository.insertBatch(accounts);

        LOGGER.info("insert done size = {}", accounts.size());

    }
}
