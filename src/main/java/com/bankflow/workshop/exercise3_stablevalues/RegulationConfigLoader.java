package com.bankflow.workshop.exercise3_stablevalues;

import java.lang.StableValue;
import java.util.Map;

/**
 * SOLUTION: Exercise 3 — Stable Values (JEP 502)
 *
 * Same pattern: StableValue replaces volatile + double-checked locking.
 */
public class RegulationConfigLoader {

    public record RegulationConfig(
            String region,
            double maxTransactionAmount,
            boolean sanctionCheckRequired,
            Map<String, String> additionalRules
    ) {}

    private final StableValue<RegulationConfig> config = StableValue.of();

    public RegulationConfig getConfig() {
        return config.orElseSet(this::loadConfig);
    }

    private RegulationConfig loadConfig() {
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
