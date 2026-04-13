package com.bankflow.workshop.domain;

import java.time.Instant;

public record AuditInfo(
        String userId,
        String correlationId,
        String sourceIp,
        Instant timestamp
) {
    public AuditInfo(String userId, String correlationId, String sourceIp) {
        this(userId, correlationId, sourceIp, Instant.now());
    }
}
