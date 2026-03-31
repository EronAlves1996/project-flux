package com.eronalves.projectflux;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEvent(String id, Instant timestamp, BigDecimal amount, String currency,
    String userId) {
}
