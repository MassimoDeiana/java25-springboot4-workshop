package com.bankflow.workshop.exercise6_httpclient;

import com.bankflow.workshop.domain.ComplianceResult;
import org.springframework.web.client.RestClient;

/**
 * TODO: Exercise 6 — Declarative HTTP Service Clients
 *
 * CURRENT STATE (before):
 * - Manual RestClient calls with explicit URL building
 * - Error handling scattered across methods
 * - Every method repeats the same boilerplate
 * - Hard to test in isolation
 *
 * YOUR TASK (after):
 * 1. Create a new interface `ComplianceServiceClient` with @HttpExchange annotations
 * 2. Create a configuration class to wire it via HttpServiceProxyFactory
 * 3. Replace this class with usage of the declarative interface
 *
 * The interface should define:
 * - POST /api/compliance/checks → validate(ComplianceRequest request) → ComplianceResult
 * - GET /api/compliance/checks/{id} → getResult(String id) → ComplianceResult
 * - GET /api/compliance/health → healthCheck() → String
 */
public class ComplianceRestClient {

    private final RestClient restClient;

    public ComplianceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Submit a compliance check.
     */
    public ComplianceResult validate(ComplianceRequest request) {
        return restClient.post()
                .uri("/api/compliance/checks")
                .body(request)
                .retrieve()
                .body(ComplianceResult.class);
    }

    /**
     * Get a compliance check result by ID.
     */
    public ComplianceResult getResult(String checkId) {
        return restClient.get()
                .uri("/api/compliance/checks/{id}", checkId)
                .retrieve()
                .body(ComplianceResult.class);
    }

    /**
     * Health check for the compliance service.
     */
    public String healthCheck() {
        return restClient.get()
                .uri("/api/compliance/health")
                .retrieve()
                .body(String.class);
    }
}
