package com.eronalves.projectflux.ingestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
    List<TransactionEvent> list = getBatchList(batchSize);
    storage.store(list);
    return list;
  }

  private List<TransactionEvent> getBatchList(int batchSize) {
    List<TransactionEvent> list =
        Stream.generate(() -> generator.generate()).limit(batchSize).toList();
    IO.println("Ingested " + batchSize + " events!!");
    return list;
  }

  public List<List<TransactionEvent>> ingestParallel(int totalEvents, int batchSize) {
    if (totalEvents <= 0) {
      throw new IllegalArgumentException("Total events should be more than or equal one");
    }

    List<CompletableFuture<List<TransactionEvent>>> submitedTasks = new ArrayList<>();

    for (int i = 0; i < totalEvents; i++) {
      submitedTasks
          .add(CompletableFuture.supplyAsync(() -> getBatchList(batchSize), executorService));
    }

    CompletableFuture.allOf(submitedTasks.stream().toArray(CompletableFuture[]::new)).join();

    return submitedTasks.stream().map(t -> {
      try {
        List<TransactionEvent> list = t.get();
        storage.store(list);
        return list;
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
        return Collections.<TransactionEvent>emptyList();
      }
    }).toList();
  }

  public StorageSink<TransactionEvent> getStorage() {
    return storage;
  }
}
