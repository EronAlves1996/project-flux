package com.eronalves.projectflux;

import com.eronalves.projectflux.config.PipelineConfig;
import com.eronalves.projectflux.generator.DataGenerator;
import com.eronalves.projectflux.ingestion.IngestionService;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.MaskedEnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.orchestrator.PipelineOrchestrator;
import com.eronalves.projectflux.serving.AnalyticsService;
import com.eronalves.projectflux.storage.StorageSink;
import com.eronalves.projectflux.transformers.TransformationService;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        PipelineConfig environment = PipelineConfig.loadEnvironment();

        StorageSink<TransactionEvent> bronzeSink = StorageSink.inMemory();
        IngestionService ingestionService =
                new IngestionService(DataGenerator.randomTransactionEventGenerator(), bronzeSink);
        StorageSink<EnrichedTransactionEvent> silverSink = StorageSink.inMemoryGenericSink();
        var transformationService = new TransformationService(silverSink);

        StorageSink<MaskedEnrichedTransactionEvent> goldenSink = StorageSink.inMemoryGenericSink();
        var analyticsService = new AnalyticsService(goldenSink);

        var orchestrator = new PipelineOrchestrator(ingestionService, transformationService,
                analyticsService, goldenSink);
        IO.println(orchestrator.execute(environment.batchSize(), environment.maskingStrategy()));
    }
}
