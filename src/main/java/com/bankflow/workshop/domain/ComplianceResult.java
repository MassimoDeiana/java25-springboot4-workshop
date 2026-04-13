package com.bankflow.workshop.domain;

public record ComplianceResult(
        String checkId,
        boolean approved,
        String reason,
        RiskLevel riskLevel
) {
    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
