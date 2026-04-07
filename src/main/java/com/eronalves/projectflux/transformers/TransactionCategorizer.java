package com.eronalves.projectflux.transformers;

import java.math.BigDecimal;
import com.eronalves.projectflux.model.TransactionCategory;

public class TransactionCategorizer {

  public static TransactionCategory categorize(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.valueOf(100)) < 0) {
      return TransactionCategory.SMALL;
    }

    if (amount.compareTo(BigDecimal.valueOf(300)) < 0) {
      return TransactionCategory.MEDIUM;
    }

    return TransactionCategory.LARGE;
  }
}
