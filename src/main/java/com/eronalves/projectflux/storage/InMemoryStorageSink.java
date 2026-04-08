package com.eronalves.projectflux.storage;

import java.util.LinkedList;
import java.util.List;

class InMemoryStorageSink<T> implements StorageSink<T> {

  private final List<List<T>> storage;

  public InMemoryStorageSink() {
    storage = new LinkedList<>();
  }

  @Override
  public void store(List<T> items) {
    storage.add(items);
    IO.println("Stored 1 batch!!");
  }

  @Override
  public List<List<T>> getAllBatches() {
    return storage;
  }

}
