package com.eronalves.projectflux.storage;

import java.util.List;
import com.eronalves.projectflux.TransactionEvent;

public interface StorageSink<T> {

  static StorageSink<TransactionEvent> inMemory() {
    return new InMemoryStorageSink();
  }

  void store(List<T> items);

}
