package com.eronalves.projectflux.orchestrator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.eronalves.projectflux.ingestion.IngestionService;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.serving.AnalyticsService;
import com.eronalves.projectflux.transformers.TransformationService;

public class PipelineOrchestrator {

  private final IngestionService ingestionService;
  private final TransformationService transformationService;
  private final AnalyticsService analyticsService;

  public PipelineOrchestrator(AnalyticsService analyticsService,
      TransformationService transformationService, IngestionService ingestionService) {
    this.analyticsService = analyticsService;
    this.transformationService = transformationService;
    this.ingestionService = ingestionService;
  }

  public PipelineRun execute(int batchSize) {
    Instant startTime = Instant.now();
    int recordsProcessed = 0;
    UUID runId = UUID.randomUUID();

    try {
      recordsProcessed += runPipeline(batchSize);
    } catch (Exception ex) {
      Instant endTime = Instant.now();
      PipelineLogger.error(runId.toString(), "Error while running pipeline", ex);
      return new PipelineRun(runId, startTime, endTime, recordsProcessed, PipelineStatus.FAILED);
    }

    Instant endTime = Instant.now();
    return new PipelineRun(runId, startTime, endTime, recordsProcessed, PipelineStatus.SUCCESS);
  }

  private int runPipeline(int batchSize) {
    ingestionService.ingestBatch(batchSize);
    int recordsProcessed = 0;

    for (List<TransactionEvent> batch : ingestionService.getStorage().getAllBatches()) {
      transformationService.transformAndStore(batch);
      recordsProcessed += batch.size();
    }

    analyticsService.getTotalAmountByCategory().entrySet().stream().forEach(IO::println);
    return recordsProcessed;
  }

}
