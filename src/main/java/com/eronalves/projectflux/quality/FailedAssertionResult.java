package com.eronalves.projectflux.quality;

record FailedAssertionResult(String errorMessage, Severity severity) implements AssertionResult {

  @Override
  public boolean passed() {
    return false;
  }

}
