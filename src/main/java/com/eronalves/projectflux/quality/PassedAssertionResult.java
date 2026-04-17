package com.eronalves.projectflux.quality;

record PassedAssertionResult() implements AssertionResult {

  @Override
  public boolean passed() {
    return true;
  }

  @Override
  public String errorMessage() {
    return "";
  }

  @Override
  public Severity severity() {
    return Severity.PASSED;
  }

}
