package com.eronalves.projectflux.orchestrator;

import java.time.Instant;
import java.util.UUID;

public record PipelineRun(UUID runId, Instant startTime, Instant endTime, int recordsProcessed,
    PipelineStatus status) {
}
