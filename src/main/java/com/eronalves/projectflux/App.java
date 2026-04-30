package com.eronalves.projectflux;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.eronalves.projectflux.config.PipelineConfig;
import com.eronalves.projectflux.generator.DataGenerator;
import com.eronalves.projectflux.ingestion.IngestionService;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.MaskedEnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEventV1;
import com.eronalves.projectflux.observability.PipelineMetrics;
import com.eronalves.projectflux.orchestrator.PipelineOrchestrator;
import com.eronalves.projectflux.serving.AnalyticsService;
import com.eronalves.projectflux.storage.StorageSink;
import com.eronalves.projectflux.stream.TransactionEventPublisher;
import com.eronalves.projectflux.stream.TransactionEventSubscriber;
import com.eronalves.projectflux.transformers.TransformationService;

/**
 * Hello world!
 */
public class App {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        try (var publisher = new TransactionEventPublisher();
                var scheduler = Executors.newScheduledThreadPool(1)) {
            StorageSink<EnrichedTransactionEvent> storage = StorageSink.inMemoryGenericSink();
            PipelineMetrics pipelineMetrics = new PipelineMetrics();

            scheduler.scheduleAtFixedRate(() -> {
                try {
                    PipelineLogger.info(null, "METRICS = " + pipelineMetrics.snapshot());
                } catch (Exception ex) {
                    PipelineLogger.error(null, "An error occurred", ex);
                }
            }, 0, 2, TimeUnit.SECONDS);

            var subscriber = new TransactionEventSubscriber(storage, pipelineMetrics);
            publisher.subscribe(subscriber);
            Thread.sleep(5000);
        } catch (Exception ex) {
            IO.println("An error has ocurred!!");
            IO.println(ex);
        }
    }

    public static void runBatch() {
        PipelineConfig environment = PipelineConfig.loadEnvironment();

        StorageSink<TransactionEventV1> bronzeSink = StorageSink.inMemory();
        IngestionService ingestionService = new IngestionService(
                DataGenerator.corruptedTransactionEventGenerator(), bronzeSink, EXECUTOR_SERVICE);
        StorageSink<EnrichedTransactionEvent> silverSink = StorageSink.inMemoryGenericSink();
        var transformationService = new TransformationService(silverSink);

        StorageSink<MaskedEnrichedTransactionEvent> goldenSink = StorageSink.inMemoryGenericSink();
        var analyticsService = new AnalyticsService(goldenSink);

        var orchestrator = new PipelineOrchestrator(ingestionService, transformationService,
                analyticsService, goldenSink);
        IO.println(orchestrator.execute(environment.batchSize(), environment.totalEvents(),
                environment.maskingStrategy()));

        EXECUTOR_SERVICE.shutdown();
    }
}
