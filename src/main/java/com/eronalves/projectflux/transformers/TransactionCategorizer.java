package com.eronalves.projectflux.transformers;

import java.math.BigDecimal;
import com.eronalves.projectflux.model.TransactionCategory;

public class TransactionCategorizer {

  private static final BigDecimal MEDIUM_TRANSACTION_MAXIMUM = BigDecimal.valueOf(300);
  private static final BigDecimal SMALL_TRANSACTION_MAXIMUM = BigDecimal.valueOf(100);

  public static TransactionCategory categorize(BigDecimal amount) {
    if (amount.compareTo(SMALL_TRANSACTION_MAXIMUM) < 0) {
      return TransactionCategory.SMALL;
    }

    if (amount.compareTo(MEDIUM_TRANSACTION_MAXIMUM) < 0) {
      return TransactionCategory.MEDIUM;
    }

    return TransactionCategory.LARGE;
  }
}
