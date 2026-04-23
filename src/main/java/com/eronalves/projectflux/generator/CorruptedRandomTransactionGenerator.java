package com.eronalves.projectflux.generator;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import com.eronalves.projectflux.model.TransactionEventV1;

public class CorruptedRandomTransactionGenerator extends RandomTransactionGenerator {

  int counter = 0;

  @Override
  public TransactionEventV1 generate() {
    counter++;
    var uncorruptedEvent = super.generate();

    if (counter % 10 == 0) {
      return new TransactionEventV1(uncorruptedEvent.id(),
          uncorruptedEvent.timestamp().minus(6, ChronoUnit.MINUTES), new BigDecimal("-10"),
          uncorruptedEvent.currency(), uncorruptedEvent.userId());
    }

    return uncorruptedEvent;
  }


}
