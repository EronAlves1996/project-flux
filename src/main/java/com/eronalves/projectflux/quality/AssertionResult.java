package com.eronalves.projectflux.quality;

public interface AssertionResult {

  static AssertionResult succeeded() {
    return new PassedAssertionResult();
  }

  static AssertionResult failed(String errorMessage, Severity severity) {
    return new FailedAssertionResult(errorMessage, severity);
  }

  enum Severity {
    WARN, ERROR, BLOCK, PASSED
  }

  boolean passed();

  String errorMessage();

  Severity severity();

}
