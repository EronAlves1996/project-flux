package com.eronalves.projectflux.stream;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import com.eronalves.projectflux.generator.DataGenerator;
import com.eronalves.projectflux.model.TransactionEventV1;

public class TransactionEventPublisher implements Publisher<TransactionEventV1>, AutoCloseable {

  private final ScheduledExecutorService executor;
  private final DataGenerator<TransactionEventV1> generator;

  public TransactionEventPublisher() {
    this(Executors.newScheduledThreadPool(1), DataGenerator.randomTransactionEventGenerator());
  }

  public TransactionEventPublisher(ScheduledExecutorService executor,
      DataGenerator<TransactionEventV1> generator) {
    this.executor = executor;
    this.generator = generator;
  }

  @Override
  public void subscribe(Subscriber<? super TransactionEventV1> subscriber) {
    executor.scheduleAtFixedRate(() -> subscriber.onNext(generator.generate()), 0, 100,
        TimeUnit.MILLISECONDS);
  }

  @Override
  public void close() throws Exception {
    executor.shutdown();
  }


}
