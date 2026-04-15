package com.eronalves.projectflux.orchestrator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.eronalves.projectflux.ingestion.IngestionService;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.model.MaskedEnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.security.DataMaskingService;
import com.eronalves.projectflux.security.MaskingStrategy;
import com.eronalves.projectflux.serving.AnalyticsService;
import com.eronalves.projectflux.storage.StorageSink;
import com.eronalves.projectflux.transformers.TransformationService;

public class PipelineOrchestrator {

  private final IngestionService ingestionService;
  private final TransformationService transformationService;
  private final AnalyticsService analyticsService;
  private final StorageSink<MaskedEnrichedTransactionEvent> goldenSink;

  public PipelineOrchestrator(IngestionService ingestionService,
      TransformationService transformationService, AnalyticsService analyticsService,
      StorageSink<MaskedEnrichedTransactionEvent> goldenSink) {
    this.ingestionService = ingestionService;
    this.transformationService = transformationService;
    this.analyticsService = analyticsService;
    this.goldenSink = goldenSink;
  }

  public PipelineRun execute(int batchSize, MaskingStrategy strategy) {
    Instant startTime = Instant.now();
    int recordsProcessed = 0;
    UUID runId = UUID.randomUUID();

    PipelineLogger.info(runId.toString(), "Starting processing");
    try {
      recordsProcessed = runPipeline(batchSize, strategy);
    } catch (Exception ex) {
      Instant endTime = Instant.now();
      PipelineLogger.error(runId.toString(), "Error while running pipeline", ex);
      return new PipelineRun(runId, startTime, endTime, recordsProcessed, PipelineStatus.FAILED);
    }

    Instant endTime = Instant.now();
    return new PipelineRun(runId, startTime, endTime, recordsProcessed, PipelineStatus.SUCCESS);
  }

  private int runPipeline(int batchSize, MaskingStrategy strategy) {
    ingestionService.ingestBatch(batchSize);
    int recordsProcessed = 0;

    for (List<TransactionEvent> batch : ingestionService.getStorage().getAllBatches()) {
      transformationService.transformAndStore(batch);
      recordsProcessed += batch.size();
    }

    for (var batch : transformationService.getSilverSink().getAllBatches()) {
      IO.println("Unmasked values");
      IO.println(batch);
      List<MaskedEnrichedTransactionEvent> maskedValues =
          batch.stream().map(e -> DataMaskingService.mask(e, strategy)).toList();
      goldenSink.store(maskedValues);
      IO.println("Masked values");
      IO.println(maskedValues);
    }

    analyticsService.getTotalAmountByCategory().entrySet().stream().forEach(IO::println);
    return recordsProcessed;
  }

}
