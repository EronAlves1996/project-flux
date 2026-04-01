package com.eronalves.projectflux;

import java.util.stream.Stream;
import com.eronalves.projectflux.generator.DataGenerator;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        var generator = DataGenerator.randomTransactionEventGenerator();
        Stream.generate(() -> generator.generate()).limit(5).forEach(IO::println);;
    }
}
