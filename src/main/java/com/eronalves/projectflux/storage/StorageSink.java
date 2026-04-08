package com.eronalves.projectflux.storage;

import java.util.List;
import com.eronalves.projectflux.model.TransactionEvent;

public interface StorageSink<T> {

  static <T> StorageSink<T> inMemoryGenericSink() {
    return new InMemoryStorageSink<>();
  }

  static StorageSink<TransactionEvent> inMemory() {
    return inMemoryGenericSink();
  }

  void store(List<T> items);

}
