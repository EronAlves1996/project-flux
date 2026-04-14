package com.eronalves.projectflux.storage;

import java.util.List;
import java.util.function.UnaryOperator;

public class AnonimizedStorageSink<T> implements StorageSink<T> {

  private final UnaryOperator<T> anonimizer;
  private final StorageSink<T> storage;

  public AnonimizedStorageSink(UnaryOperator<T> anonimizer, StorageSink<T> storage) {
    this.anonimizer = anonimizer;
    this.storage = storage;
  }

  @Override
  public void store(List<T> items) {
    storage.store(items.stream().map(anonimizer::apply).toList());
  }

  @Override
  public List<List<T>> getAllBatches() {
    return storage.getAllBatches();
  }


}
