package com.eronalves.projectflux.generator;

import com.eronalves.projectflux.model.TransactionEvent;

public interface DataGenerator<T> {

  static DataGenerator<TransactionEvent> randomTransactionEventGenerator() {
    return new RandomTransactionGenerator();
  }

  static DataGenerator<TransactionEvent> corruptedTransactionEventGenerator() {
    return new CorruptedRandomTransactionGenerator();
  }


  T generate();

}
