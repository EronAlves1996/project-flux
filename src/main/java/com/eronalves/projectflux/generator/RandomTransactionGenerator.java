package com.eronalves.projectflux.generator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import com.eronalves.projectflux.TransactionEvent;

class RandomTransactionGenerator implements DataGenerator<TransactionEvent> {

  private static final String USD = "USD";
  private ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

  @Override
  public TransactionEvent generate() {
    return new TransactionEvent(UUID.randomUUID(), Instant.now(),
        BigDecimal.valueOf(randomGenerator.nextFloat(10, 500)), USD, UUID.randomUUID());
  }


}
