package com.eronalves.projectflux.ingestion;

import java.util.List;
import java.util.stream.Stream;
import com.eronalves.projectflux.TransactionEvent;
import com.eronalves.projectflux.generator.DataGenerator;

public class IngestionService {

  private DataGenerator<TransactionEvent> generator;

  public IngestionService(DataGenerator<TransactionEvent> generator) {
    this.generator = generator;
  }

  public List<TransactionEvent> ingestBatch(int batchSize) {
    return Stream.generate(() -> generator.generate()).limit(batchSize).toList();
  }
}
