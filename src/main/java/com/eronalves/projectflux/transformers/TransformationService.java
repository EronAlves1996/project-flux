package com.eronalves.projectflux.transformers;

import java.util.List;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEventV1;
import com.eronalves.projectflux.quality.AssertionResult;
import com.eronalves.projectflux.quality.DataQualityAssertion;
import com.eronalves.projectflux.quality.NotNullAmountAssertion;
import com.eronalves.projectflux.quality.ValidTimestampAssertion;
import com.eronalves.projectflux.storage.StorageSink;

public class TransformationService {

  private final StorageSink<EnrichedTransactionEvent> silverSink;
  private final List<DataQualityAssertion<EnrichedTransactionEvent>> assertions;

  public TransformationService(StorageSink<EnrichedTransactionEvent> silverSink) {
    this.silverSink = silverSink;
    this.assertions = List.of(new NotNullAmountAssertion(), new ValidTimestampAssertion());
  }

  public void transformAndStore(List<TransactionEventV1> batch) {
    List<EnrichedTransactionEvent> enrichedBatch = batch.stream().map(
        event -> new EnrichedTransactionEvent(TransactionCategorizer.categorize(event.amount()),
            event))
        .toList();

    enrichedBatch.forEach(e -> {
      for (var assertion : assertions) {
        AssertionResult result = assertion.assertItem(e);
        if (!result.passed()) {
          PipelineLogger.warn(null, "An assertion failed: ", result.errorMessage(),
              result.severity());
        }
      }
    });

    this.silverSink.store(enrichedBatch);
    IO.println("Stored " + enrichedBatch.size() + " events to Silver");
  }

  public StorageSink<EnrichedTransactionEvent> getSilverSink() {
    return silverSink;
  }


}
