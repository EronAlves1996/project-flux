package com.eronalves.projectflux.transformers;

import java.util.UUID;
import com.eronalves.projectflux.model.TransactionEvent;
import com.eronalves.projectflux.model.TransactionEventV1;
import com.eronalves.projectflux.model.TransactionEventV2;

public class SchemaMigrationService {

  public static TransactionEvent migrate(TransactionEvent source, int targetVersion) {
    if (source.schemaVersion() == targetVersion) {
      return source;
    }

    if (source.schemaVersion() == 1) {
      return new TransactionEventV2((TransactionEventV1) source, UUID.randomUUID());
    }

    throw new UnsupportedOperationException("Unsupported schema version");
  }
}
