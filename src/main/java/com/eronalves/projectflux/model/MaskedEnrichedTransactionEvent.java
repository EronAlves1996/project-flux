package com.eronalves.projectflux.model;

public record MaskedEnrichedTransactionEvent(String userId, TransactionCategory category,
        UnpersonalizedTransactionEvent unpersonalizedEvent) {

    public MaskedEnrichedTransactionEvent(String userId, EnrichedTransactionEvent event) {
        this(userId, event.category(), event.event().unpersonalizedEvent());
    }
}
