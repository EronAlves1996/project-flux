package com.eronalves.projectflux.generator;

import com.eronalves.projectflux.TransactionEvent;

public interface DataGenerator<T> {

  static DataGenerator<TransactionEvent> randomTransactionEventGenerator() {
    return new RandomTransactionGenerator();
  }

  T generate();

}
