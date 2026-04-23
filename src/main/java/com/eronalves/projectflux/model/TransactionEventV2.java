package com.eronalves.projectflux.model;

import java.util.UUID;

public record TransactionEventV2(TransactionEvent v1Fields, UUID merchantId) {

  private static final int SCHEMA_VERSION = 2;

  public int schemaVersion() {
    return SCHEMA_VERSION;
  }
}
