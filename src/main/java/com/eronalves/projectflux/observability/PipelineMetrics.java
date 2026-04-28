package com.eronalves.projectflux.observability;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PipelineMetrics {

  private int eventsProcessed = 0;
  private List<String> errors = new LinkedList<>();
  private List<Duration> latencies = new LinkedList<>();

  public void recordEventProcessed() {
    eventsProcessed++;
  }

  public void recordError(String error) {
    errors.add(error);
  }

  public void recordLatency(Duration latency) {
    latencies.add(latency);
  }

  public Map<String, Object> snapshot() {
    return Collections.emptyMap();
  }

}
