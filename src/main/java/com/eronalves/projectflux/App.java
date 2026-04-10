package com.eronalves.projectflux;

import java.util.List;
import com.eronalves.projectflux.generator.DataGenerator;
import com.eronalves.projectflux.ingestion.IngestionService;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.orchestrator.PipelineOrchestrator;
import com.eronalves.projectflux.serving.AnalyticsService;
import com.eronalves.projectflux.storage.StorageSink;
import com.eronalves.projectflux.transformers.TransformationService;

/**
 * Hello world!
 */
public class App {
    private static final int BATCH_SIZE = 10;

    public static void main(String[] args) {
        StorageSink<TransactionEvent> bronzeSink = StorageSink.inMemory();
        IngestionService ingestionService =
                new IngestionService(DataGenerator.randomTransactionEventGenerator(), bronzeSink);
        StorageSink<EnrichedTransactionEvent> silverSink = StorageSink.inMemoryGenericSink();
        var transformationService = new TransformationService(silverSink);
        var analyticsService = new AnalyticsService(silverSink);

        var orchestrator =
                new PipelineOrchestrator(analyticsService, transformationService, ingestionService);
        IO.println(orchestrator.execute(BATCH_SIZE));
    }
}
