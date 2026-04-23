package com.eronalves.projectflux.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UnpersonalizedTransactionEvent(UUID id, Instant timestamp, BigDecimal amount,
        String currency) {
}
