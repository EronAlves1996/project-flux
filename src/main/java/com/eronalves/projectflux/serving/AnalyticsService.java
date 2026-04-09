package com.eronalves.projectflux.serving;

import java.math.BigDecimal;
import java.util.Map;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionCategory;
import com.eronalves.projectflux.storage.StorageSink;

public class AnalyticsService {

  private final StorageSink<EnrichedTransactionEvent> silverSink;

  public AnalyticsService(StorageSink<EnrichedTransactionEvent> silverSink) {
    this.silverSink = silverSink;
  }

  public Map<TransactionCategory, BigDecimal> getTotalAmountByCategory() {

  }


}
