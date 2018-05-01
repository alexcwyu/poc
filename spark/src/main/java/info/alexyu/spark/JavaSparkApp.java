package info.alexyu.spark;

import com.google.common.collect.Lists;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

//import static org.apache.spark.sql.functions.col;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;

public class JavaSparkApp {

    public static void filterReduceInt(JavaSparkContext sc, List<String> items) {

        int result =  sc.parallelize(items).flatMap(x -> Arrays.asList(x.split("/")).iterator())
                .filter(s -> s.matches("[0-9]+"))
                .map(s -> Integer.parseInt(s))
                .reduce((total, next) -> total + next);

        System.out.println(result);
    }


    public static void readCountFile(JavaSparkContext sc, String path) {

        JavaRDD<String> lines = sc.textFile(path);
        JavaPairRDD<String, Integer> pairs = lines.mapToPair(s -> new Tuple2(s,1));
        JavaPairRDD<String, Integer> counts = pairs.reduceByKey((a, b) -> a + b);
        List<Tuple2<String, Integer>> result= counts.collect();

        System.out.println(result);

    }


    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Java Spark Test").setMaster("local");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {

            //filterReduceInt(sc, Lists.newArrayList("123/643/7563/2134/ALPHA", "2343/6356/BETA/2342/12", "23423/656/343"));

            readCountFile(sc, "/home/alex/.xfce4-session.verbose-log");

        }
    }
}
