package com.eronalves.projectflux.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface TransactionEvent {


  UUID id();

  UUID userId();

  Instant timestamp();

  BigDecimal amount();

  String currency();

  int schemaVersion();
}

