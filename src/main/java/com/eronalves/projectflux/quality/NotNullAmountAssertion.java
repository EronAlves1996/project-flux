package com.eronalves.projectflux.quality;

import java.math.BigDecimal;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.quality.AssertionResult.Severity;

public class NotNullAmountAssertion implements DataQualityAssertion<EnrichedTransactionEvent> {

  private static final String NULL_OR_NEGATIVE_AMOUNT = "Null or negative amount";

  @Override
  public AssertionResult assertItem(EnrichedTransactionEvent item) {
    var amount = item.event().amount();

    if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
      return AssertionResult.failed(NULL_OR_NEGATIVE_AMOUNT, Severity.ERROR);
    }

    return AssertionResult.succeeded();
  }

}
