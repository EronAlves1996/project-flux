package com.eronalves.projectflux.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PipelineLogger {

  private static final String WARN = "WARN";
  private static final String JOINER_DELIMITER = ", ";
  private static final String WHITESPACE = " ";
  private static final String LOG_SECTION_CLOSER_DELIMITER = "] ";
  private static final String LOG_SECTION_OPENER_DELIMITER = "[";
  private static final String INFO = "INFO";
  private static final String ERROR = "ERROR";

  private static void log(String level, String runId, String message, Object... args) {
    IO.println(LOG_SECTION_OPENER_DELIMITER + Instant.now() + LOG_SECTION_CLOSER_DELIMITER
        + LOG_SECTION_OPENER_DELIMITER + level + LOG_SECTION_CLOSER_DELIMITER + message + WHITESPACE
        + Arrays.stream(args).map(Object::toString).collect(Collectors.joining(JOINER_DELIMITER)));
  }

  public static void info(String runId, String message, Object... args) {
    log(INFO, runId, message, args);
  }

  public static void warn(String runId, String message, Object... args) {
    log(WARN, runId, message, args);
  }

  public static void error(String runId, String message, Throwable t, Object... args) {
    StringWriter sw = new StringWriter();
    t.printStackTrace(new PrintWriter(sw));

    if (args != null && args.length > 0) {
      log(ERROR, runId, message, t, sw.toString(), args);
      return;
    }

    log(ERROR, runId, message, t, sw.toString());
  }

}
