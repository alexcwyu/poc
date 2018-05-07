package net.alexyu.poc.ibm;

import com.google.common.collect.Lists;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sample {

    private static Logger LOGGER = LoggerFactory.getLogger(Sample.class);
    public static final String VERSION_DATE = "2018-03-05";
    public static final String USERNAME = "0013ab3f-5344-4203-91eb-1db0b958159f";
    public static final String PASSWORD = "tmqOrni8dUmu";
    public static final String ENVIRONMENT_ID = "723a6012-7ab7-463c-9a6a-a656a726cc8c";
    public static final String CONFIGURATION_ID = "a1aff5e4-5e82-4182-a18e-227be8b0b2f8";
    public static final String COLLECTION_ID = "6287042f-6f9e-4ee4-ade3-2ff3979dbb1d";


    public static void listAllEnvs(Discovery discovery) {

        ListEnvironmentsOptions options = new ListEnvironmentsOptions.Builder().build();
        ListEnvironmentsResponse listResponse = discovery.listEnvironments(options).execute();
        LOGGER.info("listEnv\n {}", listResponse);
    }


    public static void getEnv(Discovery discovery, String environmentId) {

        GetEnvironmentOptions getOptions = new GetEnvironmentOptions.Builder(environmentId).build();
        Environment getResponse = discovery.getEnvironment(getOptions).execute();
        LOGGER.info("getEnvInfo\n {}", getResponse);
    }


    public static void listAllConfigs(Discovery discovery, String environmentId) {
        ListConfigurationsOptions listOptions = new ListConfigurationsOptions.Builder(environmentId).build();
        ListConfigurationsResponse listResponse = discovery.listConfigurations(listOptions).execute();
        LOGGER.info("listAllConfigs\n {}", listResponse);
    }

    public static void getConfig(Discovery discovery, String environmentId, String configurationId) {

        GetConfigurationOptions getOptions = new GetConfigurationOptions.Builder(environmentId, configurationId).build();
        Configuration getResponse = discovery.getConfiguration(getOptions).execute();
        LOGGER.info("getConfig\n {}", getResponse);

    }


    public static void listAllCollections(Discovery discovery, String envId) {
        ListCollectionsOptions listOptions = new ListCollectionsOptions.Builder(envId).build();
        ListCollectionsResponse listResponse = discovery.listCollections(listOptions).execute();
        LOGGER.info("listAllCollections\n {}", listResponse);
    }

    public static void getCollection(Discovery discovery, String environmentId, String collectionId) {

        GetCollectionOptions getOptions = new GetCollectionOptions.Builder(environmentId, collectionId).build();
        Collection getResponse = discovery.getCollection(getOptions).execute();
        LOGGER.info("getCollection\n {}", getResponse);


    }


    private static void queryTopStories(Discovery discovery, String environmentId, String collectionId, String query, String crawlDateFrom, String crawlDateTo) {
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
        queryBuilder
                .query(query)
                .filter("language:(english|en),crawl_date>" + crawlDateFrom + ",crawl_date<" + crawlDateTo)
                .deduplicate(true)
                .deduplicateField("title")
                .returnFields(Lists.newArrayList("title", "url", "host", "crawl_date"));

        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();
        LOGGER.info("queryResponse\n {}", queryResponse);
    }



    private static void querySentimentAnalysis(Discovery discovery, String environmentId, String collectionId, String query, String crawlDateFrom, String crawlDateTo) {
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
        queryBuilder
                .query(query)
                .filter("language:(english|en),crawl_date>" + crawlDateFrom + ",crawl_date<" + crawlDateTo)
                .aggregation("term(host).term(enriched_text.sentiment.document.label)");

        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();
        LOGGER.info("queryResponse\n {}", queryResponse);
    }


    private static void querySentimentAnalysis2(Discovery discovery, String environmentId, String collectionId, String query, String crawlDateFrom, String crawlDateTo) {
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
        queryBuilder
                .query(query)
                .filter("language:(english|en),crawl_date>" + crawlDateFrom + ",crawl_date<" + crawlDateTo)
                .aggregation("term(host).term(enriched_text.sentiment.document.label)");

        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();
        LOGGER.info("queryResponse\n {}", queryResponse);
    }

    public static void main(String[] args) throws Exception {
        Discovery discovery = new Discovery(
                VERSION_DATE,
                USERNAME,
                PASSWORD);


//        listAllEnvs(discovery);
//        getEnv(discovery, "system");
//
//        listAllConfigs(discovery, "system");
//
//        listAllCollections(discovery, "system");
//        getCollection(discovery, "system", "news-en");


        //queryTopStories(discovery, "system", "news-en", "baba", "2018-03-08T12:00:00+0800", "2018-05-08T12:00:00+0800");
        //querySentimentAnalysis(discovery, "system", "news-en", "baba", "2018-03-08T12:00:00+0800", "2018-05-08T12:00:00+0800");
        querySentimentAnalysis2(discovery, "system", "news-en", "baba", "2018-03-08T12:00:00+0800", "2018-05-08T12:00:00+0800");

    }

}
