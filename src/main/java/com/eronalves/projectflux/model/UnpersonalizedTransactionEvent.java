package com.eronalves.projectflux.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UnpersonalizedTransactionEvent(UUID id, Instant timestamp, BigDecimal amount,
        String currency) {

    private static final int SCHEMA_VERSION = 1;

    public int schemaVersion() {
        return SCHEMA_VERSION;
    }
}
