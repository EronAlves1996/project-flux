package com.eronalves.projectflux.config;

import java.util.Arrays;
import java.util.regex.Pattern;
import com.eronalves.projectflux.logging.PipelineLogger;
import com.eronalves.projectflux.security.MaskingStrategy;

public record PipelineConfig(int batchSize, MaskingStrategy maskingStrategy,
    Environment environment, int totalEvents) {

  public PipelineConfig(int batchSize, MaskingStrategy maskingStrategy, Environment environment) {
    this(batchSize, maskingStrategy, environment, 10);
  }

  private static final String FLUX_ENV = "FLUX_ENV";
  private static final String FLUX_MASKING_STRATEGY = "FLUX_MASKING_STRATEGY";
  private static final String FLUX_BATCH_SIZE = "FLUX_BATCH_SIZE";
  private static final String FLUX_TOTAL_EVENTS = "FLUX_TOTAL_EVENTS";

  private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

  private static <T extends Enum<T>> T envEnumValue(String environmentVariable, Class<T> enumClass,
      T defaultValue) {
    var variable = System.getenv(environmentVariable);
    if (variable == null || variable.isEmpty()) {
      PipelineLogger.warn(null, environmentVariable + " not defined, fallbacking to default value ("
          + defaultValue + ")");
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

  private static int envIntValue(String environmentVariable, int defaultValue) {
    String value = System.getenv(environmentVariable);

    if (value == null || value.isEmpty()) {
      PipelineLogger.warn(null, environmentVariable
          + " not configured, fallbacking to default value (" + defaultValue + ")");
      return defaultValue;
    }


    if (!INTEGER_PATTERN.matcher(value).matches()) {
      throw new IllegalStateException(environmentVariable + " should be numeric");
    }

    return Integer.valueOf(value);
  }

  public static PipelineConfig loadEnvironment() {
    int parsedFluxBatchSize = envIntValue(FLUX_BATCH_SIZE, 10);
    int parsedFluxTotalEvents = envIntValue(FLUX_TOTAL_EVENTS, 10);

    MaskingStrategy strategy =
        envEnumValue(FLUX_MASKING_STRATEGY, MaskingStrategy.class, MaskingStrategy.HASH_SHA256);

    Environment env = envEnumValue(FLUX_ENV, Environment.class, Environment.DEV);

    return new PipelineConfig(parsedFluxBatchSize, strategy, env, parsedFluxTotalEvents);

  }

}
