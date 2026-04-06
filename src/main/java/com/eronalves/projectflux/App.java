package com.eronalves.projectflux;

import com.eronalves.projectflux.generator.DataGenerator;
import com.eronalves.projectflux.ingestion.IngestionService;

/**
 * Hello world!
 */
public class App {
    private static final int BATCH_SIZE = 10;

    public static void main(String[] args) {
        new IngestionService(DataGenerator.randomTransactionEventGenerator())
                .ingestBatch(BATCH_SIZE).forEach(IO::println);
    }
}
