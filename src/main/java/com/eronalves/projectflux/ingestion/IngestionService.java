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
import com.eronalves.projectflux.model.TransactionEventV1;
import com.eronalves.projectflux.storage.StorageSink;

public class IngestionService {

  private final DataGenerator<TransactionEventV1> generator;
  private final StorageSink<TransactionEventV1> storage;
  private final ExecutorService executorService;

  public IngestionService(DataGenerator<TransactionEventV1> generator,
      StorageSink<TransactionEventV1> storage, ExecutorService executorService) {
    this.generator = generator;
    this.storage = storage;
    this.executorService = executorService;
  }

  public List<TransactionEventV1> ingestBatch(int batchSize) {
    List<TransactionEventV1> list = getBatchList(batchSize);
    storage.store(list);
    return list;
  }

  private List<TransactionEventV1> getBatchList(int batchSize) {
    List<TransactionEventV1> list =
        Stream.generate(() -> generator.generate()).limit(batchSize).toList();
    IO.println("Ingested " + batchSize + " events!!");
    return list;
  }

  public List<List<TransactionEventV1>> ingestParallel(int totalEvents, int batchSize) {
    if (totalEvents <= 0) {
      throw new IllegalArgumentException("Total events should be more than or equal one");
    }

    List<CompletableFuture<List<TransactionEventV1>>> submitedTasks = new ArrayList<>();

    for (int i = 0; i < totalEvents; i += batchSize) {
      submitedTasks
          .add(CompletableFuture.supplyAsync(() -> getBatchList(batchSize), executorService));
    }

    CompletableFuture.allOf(submitedTasks.stream().toArray(CompletableFuture[]::new)).join();

    return submitedTasks.stream().map(t -> {
      try {
        List<TransactionEventV1> list = t.get();
        storage.store(list);
        return list;
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
        return Collections.<TransactionEventV1>emptyList();
      }
    }).toList();
  }

  public StorageSink<TransactionEventV1> getStorage() {
    return storage;
  }
}
