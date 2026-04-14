package com.eronalves.projectflux.serving;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import com.eronalves.projectflux.model.MaskedEnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionCategory;
import com.eronalves.projectflux.storage.StorageSink;

public class AnalyticsService {

  private final StorageSink<MaskedEnrichedTransactionEvent> silverSink;

  public AnalyticsService(StorageSink<MaskedEnrichedTransactionEvent> silverSink) {
    this.silverSink = silverSink;
  }

  public Map<TransactionCategory, BigDecimal> getTotalAmountByCategory() {
    return this.silverSink.getAllBatches().stream().flatMap(Collection::stream)
        .collect(Collectors.groupingBy(MaskedEnrichedTransactionEvent::category, Collectors
            .reducing(BigDecimal.ZERO, a -> a.unpersonalizedEvent().amount(), BigDecimal::add)));
  }

}
