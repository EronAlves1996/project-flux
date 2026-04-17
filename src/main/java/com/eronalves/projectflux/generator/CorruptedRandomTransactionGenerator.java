package com.eronalves.projectflux.generator;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import com.eronalves.projectflux.model.TransactionEvent;

public class CorruptedRandomTransactionGenerator extends RandomTransactionGenerator {

  int counter = 0;

  @Override
  public TransactionEvent generate() {
    counter++;
    var uncorruptedEvent = super.generate();

    if (counter % 10 == 0) {
      return new TransactionEvent(uncorruptedEvent.id(),
          uncorruptedEvent.timestamp().minus(6, ChronoUnit.MINUTES), new BigDecimal("-10"),
          uncorruptedEvent.currency(), uncorruptedEvent.userId());
    }

    return uncorruptedEvent;
  }


}
