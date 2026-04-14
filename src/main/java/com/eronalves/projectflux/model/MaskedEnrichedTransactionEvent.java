package com.eronalves.projectflux.model;

public record MaskedEnrichedTransactionEvent(String userId, TransactionCategory category,
    UnpersonalizedTransactionEvent unpersonalizedEvent) {
}
