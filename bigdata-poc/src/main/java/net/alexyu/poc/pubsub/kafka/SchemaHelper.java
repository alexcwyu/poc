package net.alexyu.poc.pubsub.kafka;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SchemaHelper {


    private final String hostPort;
    private final OkHttpClient client = new OkHttpClient();

    private final static MediaType SCHEMA_CONTENT =
            MediaType.parse("application/vnd.schemaregistry.v1+json");

    public SchemaHelper(String hostPort) {
        this.hostPort = hostPort;
    }

    private String send(Request request) throws IOException {
        return client.newCall(request).execute().body().string();
    }

    public String postNewSchema(String schema, String schemaContent) throws IOException {
        Request request = new Request.Builder()
                .post(RequestBody.create(SCHEMA_CONTENT, schemaContent))
                .url("http://" + hostPort + "/subjects/" + schema + "/versions")
                .build();
        return send(request);
    }

    public String listAllSchema() throws IOException {
        Request request = new Request.Builder()
                .url("http://" + hostPort + "/subjects")
                .build();
        return send(request);
    }

    public String showAllVersions(String schema) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + hostPort + "/subjects/" + schema + "/versions")
                .build();
        return send(request);
    }


    public String showByVersion(String schema, String version) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + hostPort + "/subjects/" + schema + "/versions/" + version)
                .build();
        return send(request);
    }


    public String showById(String id) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + hostPort + "/schemas/ids/" + id)
                .build();
        return send(request);
    }


    public String showByLatestVersion(String schema) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + hostPort + "/subjects/" + schema + "/versions/latest")
                .build();
        return send(request);
    }

    public String checkIfRegistered(String schema, String schemaContent) throws IOException {
        Request request = new Request.Builder()
                .post(RequestBody.create(SCHEMA_CONTENT, schemaContent))
                .url("http://" + hostPort + "/subjects/" + schema)
                .build();
        return send(request);
    }


    public String testCompatibility(String schema, String schemaContent) throws IOException {
        Request request = new Request.Builder()
                .post(RequestBody.create(SCHEMA_CONTENT, schemaContent))
                .url("http://" + hostPort + "/compatibility/subjects/" + schema + "/versions/latest")
                .build();
        return send(request);
    }

    public String getTopLevelConfig() throws IOException {
        Request request = new Request.Builder()
                .url("http://" + hostPort + "/config")
                .build();
        return send(request);
    }


    public String setTopLevelConfig(String config) throws IOException {
        Request request = new Request.Builder()
                .put(RequestBody.create(SCHEMA_CONTENT, config))
                .url("http://" + hostPort + "/config")
                .build();
        return send(request);
    }


    public String setConfigForSchema(String schema, String config) throws IOException {
        Request request = new Request.Builder()
                .put(RequestBody.create(SCHEMA_CONTENT, config))
                .url("http://" + hostPort + "/config/" + schema)
                .build();
        return send(request);
    }


    public static String readSchema(String path) throws Exception {
//        ClassLoader classLoader = SchemaHelper.class.getClassLoader();
//        File file = new File(classLoader.getResource(path).getFile());
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{\"schema\":\""+(contentBuilder.toString().replace("\"", "\\\"").replace("\n", "").replace("\r", "")) +"\"}";

    }

    public static void main(String... args) throws Exception {
        SchemaHelper schemaHelper = new SchemaHelper("localhost:8081");

        String result = schemaHelper.postNewSchema("instrument-value", readSchema("/mnt/data/dev/workspaces/java8/bigdata-poc/src/main/resources/avro/instrument.avsc"));
        System.out.println(result);

        result = schemaHelper.postNewSchema("marketdata-key", readSchema("/mnt/data/dev/workspaces/java8/bigdata-poc/src/main/resources/avro/marketdata.avsc"));
        System.out.println(result);

        result = schemaHelper.postNewSchema("trade-value", readSchema("/mnt/data/dev/workspaces/java8/bigdata-poc/src/main/resources/avro/trade.avsc"));
        System.out.println(result);
    }

}


