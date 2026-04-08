package com.eronalves.projectflux.transformers;

import java.util.List;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.storage.StorageSink;

public class TransformationService {

  private final StorageSink<EnrichedTransactionEvent> silverSink;

  public TransformationService(StorageSink<EnrichedTransactionEvent> silverSink) {
    this.silverSink = silverSink;
  }

  public void transformAndStore(List<TransactionEvent> batch) {
    List<EnrichedTransactionEvent> enrichedBatch = batch.stream().map(
        event -> new EnrichedTransactionEvent(TransactionCategorizer.categorize(event.amount()),
            event))
        .toList();
    this.silverSink.store(enrichedBatch);
  }

}
