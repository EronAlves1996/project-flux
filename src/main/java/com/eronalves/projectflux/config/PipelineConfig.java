package com.eronalves.projectflux.config;

import java.util.Arrays;
import java.util.regex.Pattern;
import com.eronalves.projectflux.security.MaskingStrategy;

public record PipelineConfig(int batchSize, MaskingStrategy maskingStrategy,
    Environment environment) {

  private static final String FLUX_ENV = "FLUX_ENV";
  private static final String FLUX_MASKING_STRATEGY = "FLUX_MASKING_STRATEGY";
  private static final String FLUX_BATCH_SIZE = "FLUX_BATCH_SIZE";
  private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

  private static <T extends Enum<T>> T envEnumValue(String environmentVariable, Class<T> enumClass,
      T defaultValue) {
    var variable = System.getenv(environmentVariable);
    if (variable == null || variable.isEmpty()) {
      return defaultValue;
    }

    T parsedEnumValue = Enum.valueOf(enumClass, variable);

    if (parsedEnumValue == null) {
      throw new IllegalStateException(
          environmentVariable + " should be one of the following: " + String.join(", ",
              Arrays.stream(enumClass.getEnumConstants()).map(String::valueOf).toList()));
    }

    return parsedEnumValue;
  }


  public static PipelineConfig loadEnvironment() {
    String fluxBatchSize = System.getenv(FLUX_BATCH_SIZE);

    int parsedFluxBatchSize = 10;

    if (fluxBatchSize != null && !fluxBatchSize.isEmpty()) {
      if (!INTEGER_PATTERN.matcher(fluxBatchSize).matches()) {
        throw new IllegalStateException(FLUX_BATCH_SIZE + " should be numeric");
      }
    }

    MaskingStrategy strategy =
        envEnumValue(FLUX_MASKING_STRATEGY, MaskingStrategy.class, MaskingStrategy.HASH_SHA256);

    Environment env = envEnumValue(FLUX_ENV, Environment.class, Environment.DEV);

    return new PipelineConfig(parsedFluxBatchSize, strategy, env);

  }

}
