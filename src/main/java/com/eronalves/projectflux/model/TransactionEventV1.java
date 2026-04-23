package com.eronalves.projectflux.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionEventV1(UnpersonalizedTransactionEvent unpersonalizedEvent, UUID userId)
                                implements TransactionEvent {
                public TransactionEventV1(UUID id, Instant timestamp, BigDecimal amount,
                                                String currency, UUID userId) {
                                this(new UnpersonalizedTransactionEvent(id, timestamp, amount,
                                                                currency), userId);
                }

                public UUID id() {
                                return this.unpersonalizedEvent().id();
                }

                public Instant timestamp() {
                                return this.unpersonalizedEvent().timestamp();
                }

                public BigDecimal amount() {
                                return this.unpersonalizedEvent.amount();
                }

                public String currency() {
                                return this.unpersonalizedEvent.currency();
                }

                @Override
                public int schemaVersion() {
                                return 1;
                }
}
