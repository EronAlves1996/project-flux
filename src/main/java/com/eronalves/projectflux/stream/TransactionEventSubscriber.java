package com.eronalves.projectflux.stream;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionEventV1;
import com.eronalves.projectflux.resilience.RetryUtils;
import com.eronalves.projectflux.storage.StorageSink;
import com.eronalves.projectflux.transformers.TransactionCategorizer;

public class TransactionEventSubscriber implements Subscriber<TransactionEventV1> {

  private final StorageSink<EnrichedTransactionEvent> storage;

  public TransactionEventSubscriber(StorageSink<EnrichedTransactionEvent> storage) {
    this.storage = storage;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    PipelineLogger.info(null, "Subscribed!!");
  }

  @Override
  public void onNext(TransactionEventV1 item) {
    PipelineLogger.info(null, "Next item: ", item);
    RetryUtils.withRetry(() -> {
      storage.store(List.of(
          new EnrichedTransactionEvent(TransactionCategorizer.categorize(item.amount()), item)));
      return null;
    }, 3, Duration.ofMillis(100));

  }

  @Override
  public void onError(Throwable throwable) {
    PipelineLogger.error(null, "An error has occurred", throwable);
  }

  @Override
  public void onComplete() {
    PipelineLogger.info(null, "Completed!");
  }


}
