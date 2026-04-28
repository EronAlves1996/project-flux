package com.eronalves.projectflux.observability;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.eronalves.projectflux.logging.PipelineLogger;

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
    return Map.of("events_processed", String.valueOf(eventsProcessed), "errors",
        String.valueOf(errors.size()), "p50_latency", String.valueOf(calculateP50Latency()) + "ns");
  }

  private long calculateP50Latency() {
    if (latencies.size() == 0) {
      return 0;
    }

    if (latencies.size() == 1) {
      return latencies.getFirst().toNanos();
    }

    var sortedLatency = latencies.stream().map(d -> d.toNanos()).sorted().toList();
    var sortedLatencySize = sortedLatency.size();
    var sizeIsEven = sortedLatencySize % 2 == 0;
    var middleOfList = sortedLatencySize / 2;

    return sizeIsEven ? (sortedLatency.get(middleOfList) + sortedLatency.get(middleOfList + 1)) / 2
        : sortedLatency.get(middleOfList);
  }

}
