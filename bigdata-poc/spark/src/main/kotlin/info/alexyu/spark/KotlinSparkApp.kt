//package info.alexyu.spark
//
//import org.apache.spark.SparkConf
//import org.apache.spark.api.java.JavaSparkContext
//import java.util.*
//import kotlin.reflect.KClass
//
//
//fun main(args: Array<String>){
//    val conf = SparkConf()
//            .setMaster("local")
//            .setAppName("Kotlin Spark Test")
//
//    val sc = JavaSparkContext(conf)
//
//    val items = listOf("123/643/7563/2134/ALPHA", "2343/6356/BETA/2342/12", "23423/656/343")
//
//    val input = sc.parallelize(items)
//
//    val sumOfNumbers = input.flatMap { Arrays.asList(it.split("/")).iterator()}
//            .map {  it.toInt() }
//            .reduce {total,next -> total + next }
//
//    println(sumOfNumbers)
//
//}
//
//
//fun SparkConf.registerKryoClasses(vararg args: KClass<*>) =
//        registerKryoClasses(args.map { it.java }.toTypedArray()