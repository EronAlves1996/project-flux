package com.eronalves.projectflux.model;

public record EnrichedTransactionEvent(TransactionCategory category, TransactionEventV1 event) {
}
