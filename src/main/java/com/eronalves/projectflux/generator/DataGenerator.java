package com.eronalves.projectflux.generator;

import com.eronalves.projectflux.model.TransactionEventV1;

public interface DataGenerator<T> {

  static DataGenerator<TransactionEventV1> randomTransactionEventGenerator() {
    return new RandomTransactionGenerator();
  }

  static DataGenerator<TransactionEventV1> corruptedTransactionEventGenerator() {
    return new CorruptedRandomTransactionGenerator();
  }


  T generate();

}
