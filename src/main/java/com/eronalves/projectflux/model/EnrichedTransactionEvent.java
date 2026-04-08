package com.eronalves.projectflux.model;

public record EnrichedTransactionEvent(TransactionCategory category, TransactionEvent event) {
}
