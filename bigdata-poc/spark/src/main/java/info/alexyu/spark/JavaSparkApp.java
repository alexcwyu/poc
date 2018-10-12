package info.alexyu.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

import static org.apache.spark.sql.functions.col;

public class JavaSparkApp {


    public static void main(String[] args) throws Exception{


        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .appName("JavaSparkApp")
                .config("spark.driver.extraClassPath", "/Users/alex/dev/workspaces/poc/bigdata-poc/base/lib")
                .config("spark.jars", "/Users/alex/dev/workspaces/poc/bigdata-poc/base/lib/postgresql-42.2.2.jar")
                .getOrCreate();
        Properties connectionProperties = new Properties();


        Dataset<Row> df = spark.read().format("csv")
                .option("header","true")
                .option("inferSchema", "true")
                .load("file:///Users/alex/dev/workspaces/poc/bigdata-poc/base/src/main/resources/data/Real_Estate_Sales_By_Town_for_2011__2012__2013__2014.csv");
        df.show();

        df.createGlobalTempView("real_estate_sales");

        spark.sql("SELECT Name, sum(AssessedValue), sum(SalePrice), avg(AssessedValue), avg(SalePrice) FROM global_temp.real_estate_sales where ListYear >0 group by Name").show();


        //connectionProperties.put("user", "postgres");
        //connectionProperties.put("driver", "org.postgresql.Driver");
        //Dataset<Row> jdbcDF = spark.read()
        //                .jdbc("jdbc:postgresql://localhost:32768/algo", "accounts", connectionProperties).select("acct_id", "acct_name");

        //jdbcDF.show();

        spark.stop();

    }
}
