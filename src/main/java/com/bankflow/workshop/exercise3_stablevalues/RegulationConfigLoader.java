package com.bankflow.workshop.exercise3_stablevalues;

import java.util.Map;

/**
 * TODO: Exercise 3 — Stable Values (second class to refactor)
 *
 * Same pattern: replace volatile + double-checked locking with StableValue.
 */
public class RegulationConfigLoader {

    public record RegulationConfig(
            String region,
            double maxTransactionAmount,
            boolean sanctionCheckRequired,
            Map<String, String> additionalRules
    ) {}

    // TODO: Replace with StableValue<RegulationConfig>
    private volatile RegulationConfig config;

    public RegulationConfig getConfig() {
        // TODO: Replace with stableValue.orElseSet(this::loadConfig)
        if (config == null) {
            synchronized (this) {
                if (config == null) {
                    config = loadConfig();
                }
            }
        }
        return config;
    }

    /**
     * Simulates loading regulation config from an external source.
     * In real life, this would read from a database or config service.
     */
    private RegulationConfig loadConfig() {
        // Simulate slow loading
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        return new RegulationConfig(
                "EU",
                50_000.00,
                true,
                Map.of(
                        "PSD2_REQUIRED", "true",
                        "MAX_DAILY_TRANSFERS", "10",
                        "AML_THRESHOLD", "15000.00"
                )
        );
    }
}
