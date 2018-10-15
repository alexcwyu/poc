package info.alexyu.spark;

import com.google.common.collect.Maps;
import org.apache.spark.sql.*;


import java.util.Properties;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.to_date;

public class JavaSparkApp {


    public static void main(String[] args) throws Exception{

//        String jdbc_url = "jdbc:postgresql://172.18.0.5:5432/risk";
//        String spark_master = "spark://localhost:7077";

        String jdbc_url = "jdbc:postgresql://localhost:32768/risk";
        String spark_master = "local";

        SparkSession spark = SparkSession
                .builder()
                .master(spark_master)
                .appName("JavaSparkApp")
                .config("spark.driver.extraClassPath", "/tmp/lib")
                .config("spark.jars", "/tmp/lib/postgresql-42.2.2.jar")
                .getOrCreate();
        Properties connectionProperties = new Properties();


        Dataset<Row> df = spark.read().format("csv")
                .option("header","true")
                .option("inferSchema", "true")
                .option("dateFormat", "MM/dd/yyyy")
                .load("file:///tmp/data/Real_Estate_Sales_By_Town_for_2011__2012__2013__2014.csv");
        df.show();
        df.printSchema();

        df = df.filter("ListYear > 0");

        df = df.withColumn("DateRecorded", to_date(col("DateRecorded"), "MM/dd/yyyy"))
                .withColumn("SerialNbr", (col("SerialNbr").cast("int")));

        df.show();
        df = df.toDF("name", "serial_nbr", "list_year", "date_recorded", "assessed_value", "sale_price", "additional_remarks",
                "sales_ratio", "non_use_code", "residential_type", "residential_units", "address", "location");

        df.show();
        //df.createGlobalTempView("real_estate_sales.sql");

        //spark.sql("SELECT Name, sum(AssessedValue), sum(SalePrice), avg(AssessedValue), avg(SalePrice) FROM global_temp.real_estate_sales.sql where ListYear >0 group by Name").show();

        Properties properties = new Properties();
        properties.setProperty("user","postgres");
        df.write().mode(SaveMode.Append).jdbc(jdbc_url, "real_estate_sales", properties);

        //connectionProperties.put("user", "postgres");
        //connectionProperties.put("driver", "org.postgresql.Driver");
        //Dataset<Row> jdbcDF = spark.read()
        //                .jdbc("jdbc:postgresql://localhost:32768/algo", "accounts", connectionProperties).select("acct_id", "acct_name");

        //jdbcDF.show();

        spark.stop();

    }
}
