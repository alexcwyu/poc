package net.alexyu.poc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by alex on 3/6/17.
 */
public class TestStream {

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("Hello", "World");

        Map<String, Long> result = strings.stream().flatMap(s -> Arrays.stream(s.split(""))).collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()));

        System.out.println(result);
    }
}
