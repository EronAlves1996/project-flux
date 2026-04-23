package com.eronalves.projectflux.storage;

import java.util.List;
import com.eronalves.projectflux.model.TransactionEventV1;

public interface StorageSink<T> {

  static <T> StorageSink<T> inMemoryGenericSink() {
    return new InMemoryStorageSink<>();
  }

  static StorageSink<TransactionEventV1> inMemory() {
    return inMemoryGenericSink();
  }

  void store(List<T> items);

  List<List<T>> getAllBatches();

}
