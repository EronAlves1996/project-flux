package com.eronalves.projectflux.ingestion;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;
import com.eronalves.projectflux.generator.DataGenerator;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.storage.StorageSink;

public class IngestionService {

  private final DataGenerator<TransactionEvent> generator;
  private final StorageSink<TransactionEvent> storage;
  private final ExecutorService executorService;

  public IngestionService(DataGenerator<TransactionEvent> generator,
      StorageSink<TransactionEvent> storage, ExecutorService executorService) {
    this.generator = generator;
    this.storage = storage;
    this.executorService = executorService;
  }

  public List<TransactionEvent> ingestBatch(int batchSize) {
    List<TransactionEvent> list =
        Stream.generate(() -> generator.generate()).limit(batchSize).toList();
    IO.println("Ingested " + batchSize + " events!!");
    storage.store(list);
    return list;
  }

  public StorageSink<TransactionEvent> getStorage() {
    return storage;
  }
}
