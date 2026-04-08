package com.eronalves.projectflux.storage;

import java.util.LinkedList;
import java.util.List;
import com.eronalves.projectflux.model.TransactionEvent;

class InMemoryStorageSink implements StorageSink<TransactionEvent> {

  private final List<List<TransactionEvent>> storage;

  public InMemoryStorageSink() {
    storage = new LinkedList<>();
  }

  @Override
  public void store(List<TransactionEvent> items) {
    storage.add(items);
    IO.println("Stored 1 batch!!");
  }

}
