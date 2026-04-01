package com.eronalves.projectflux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionEvent(UUID id, Instant timestamp, BigDecimal amount, String currency,
        UUID userId) {
}
