package com.eronalves.projectflux.storage;

import java.util.List;

public interface StorageSink<T> {

  void store(List<T> items);

}
