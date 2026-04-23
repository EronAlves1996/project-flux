package com.eronalves.projectflux.generator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import com.eronalves.projectflux.model.TransactionEventV1;

class RandomTransactionGenerator implements DataGenerator<TransactionEventV1> {

  private static final int LOWER_BOUND = 10;
  private static final int UPPER_BOUND = 500;
  private static final int REAL_VALUE_TRANSFORMER_FACTOR = 100;
  private static final String USD = "USD";
  private ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

  @Override
  public TransactionEventV1 generate() {
    int transformedLowerBound = LOWER_BOUND * REAL_VALUE_TRANSFORMER_FACTOR;
    int transformedUpperBound = UPPER_BOUND * REAL_VALUE_TRANSFORMER_FACTOR;

    double randomValue =
        Long.valueOf(randomGenerator.nextLong(transformedLowerBound, transformedUpperBound))
            .doubleValue() / REAL_VALUE_TRANSFORMER_FACTOR;

    return new TransactionEventV1(UUID.randomUUID(), Instant.now(), BigDecimal.valueOf(randomValue),
        USD, UUID.randomUUID());
  }


}
