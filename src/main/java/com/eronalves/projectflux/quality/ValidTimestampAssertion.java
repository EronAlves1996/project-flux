package com.eronalves.projectflux.quality;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.quality.AssertionResult.Severity;

public class ValidTimestampAssertion implements DataQualityAssertion<EnrichedTransactionEvent> {

  private static final String EVENT_IS_MORE_THAN_FIVE_MINUTES_BEFORE =
      "Event is more than five minutes before";

  @Override
  public AssertionResult assertItem(EnrichedTransactionEvent item) {
    if (Instant.now().minus(5, ChronoUnit.MINUTES).isAfter(item.event().timestamp())) {
      return AssertionResult.failed(EVENT_IS_MORE_THAN_FIVE_MINUTES_BEFORE, Severity.ERROR);
    }

    return AssertionResult.succeeded();
  }


}
