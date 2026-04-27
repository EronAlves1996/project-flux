package com.eronalves.projectflux.resilience;

import java.time.Duration;
import java.util.function.Supplier;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.storage.TransientStorageException;

public class RetryUtils {

  public static <T> T withRetry(Supplier<T> operation, int maxAttempts, Duration initialBackoff) {
    for (int i = 0; i < maxAttempts; i++) {
      try {
        return operation.get();
      } catch (TransientStorageException ex) {
        try {
          PipelineLogger.info(null,
              "Retry " + i + 1 + "/" + maxAttempts + " after " + initialBackoff);
          Thread.sleep(initialBackoff);
        } catch (InterruptedException e) {
          throw new FailedOperationException("Failed to retry", e);
        }
      }
    }
    throw new FailedOperationException("Failed operation");
  }
}
