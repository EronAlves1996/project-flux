package com.eronalves.projectflux.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionEventV2(TransactionEventV1 v1Fields, UUID merchantId)
    implements TransactionEvent {

  private static final int SCHEMA_VERSION = 2;

  public int schemaVersion() {
    return SCHEMA_VERSION;
  }

  @Override
  public UUID id() {
    return v1Fields().id();
  }

  @Override
  public UUID userId() {
    return v1Fields().userId();
  }

  @Override
  public Instant timestamp() {
    return v1Fields().timestamp();
  }

  @Override
  public BigDecimal amount() {
    return v1Fields().amount();
  }

  @Override
  public String currency() {
    return v1Fields().currency();
  }
}
