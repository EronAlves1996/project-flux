package com.eronalves.projectflux.generator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import com.eronalves.projectflux.TransactionEvent;

class RandomTransactionGenerator implements DataGenerator<TransactionEvent> {

  private static final int LOWER_BOUND = 10;
  private static final int UPPER_BOUND = 500;
  private static final int REAL_VALUE_TRANSFORMER_FACTOR = 100;
  private static final String USD = "USD";
  private ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

  @Override
  public TransactionEvent generate() {
    int transformedLowerBound = LOWER_BOUND * REAL_VALUE_TRANSFORMER_FACTOR;
    int transformedUpperBound = UPPER_BOUND * REAL_VALUE_TRANSFORMER_FACTOR;

    double randomValue = randomGenerator.nextLong(transformedLowerBound, transformedUpperBound)
        / REAL_VALUE_TRANSFORMER_FACTOR;

    return new TransactionEvent(UUID.randomUUID(), Instant.now(), BigDecimal.valueOf(randomValue),
        USD, UUID.randomUUID());
  }


}
