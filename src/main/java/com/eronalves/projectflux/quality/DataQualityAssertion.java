package com.eronalves.projectflux.quality;

public interface DataQualityAssertion<T> {

  AssertionResult assertItem(T item);

}
