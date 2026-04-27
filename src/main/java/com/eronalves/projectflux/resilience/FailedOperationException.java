package com.eronalves.projectflux.resilience;

public class FailedOperationException extends RuntimeException {

  public FailedOperationException(String message) {
    super(message);
  }

  public FailedOperationException(String string, Throwable e) {
    super(string, e);
  }

}

