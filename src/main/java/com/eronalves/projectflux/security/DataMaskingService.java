package com.eronalves.projectflux.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import com.eronalves.projectflux.model.EnrichedTransactionEvent;
import com.eronalves.projectflux.model.MaskedEnrichedTransactionEvent;
import com.eronalves.projectflux.model.TransactionCategory;
import com.eronalves.projectflux.model.UnpersonalizedTransactionEvent;

public class DataMaskingService {

  private static final String REDACTION_CHAR = "*";
  private static final int UUID_BYTE_SIZE = 16;
  private static final String SHA_256 = "SHA-256";

  public static MaskedEnrichedTransactionEvent mask(EnrichedTransactionEvent event,
      MaskingStrategy strategy) {
    switch (strategy) {
      case HASH_SHA256:
        return maskSha256(event);
      case TOKENIZE:
        return maskTokenizing(event);
      default:
        return maskRedacting(event);
    }
  }

  private static MaskedEnrichedTransactionEvent maskRedacting(EnrichedTransactionEvent event) {
    return new MaskedEnrichedTransactionEvent(
        REDACTION_CHAR.repeat(event.event().userId().toString().length()), event);
  }

  // Mock implementation only reversing the UUID
  private static MaskedEnrichedTransactionEvent maskTokenizing(EnrichedTransactionEvent event) {
    return new MaskedEnrichedTransactionEvent(
        new StringBuilder(event.event().userId().toString()).reverse().toString(), event);
  }

  private static MaskedEnrichedTransactionEvent maskSha256(EnrichedTransactionEvent event) {
    ByteBuffer uuidBuffer = ByteBuffer.allocate(UUID_BYTE_SIZE);
    UUID userId = event.event().userId();

    uuidBuffer.putLong(userId.getMostSignificantBits());
    uuidBuffer.putLong(userId.getLeastSignificantBits());

    try {
      MessageDigest instance = MessageDigest.getInstance(SHA_256);
      byte[] hashedUUID = instance.digest(uuidBuffer.array());
      return new MaskedEnrichedTransactionEvent(new String(hashedUUID, StandardCharsets.UTF_8),
          event);
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }
}
