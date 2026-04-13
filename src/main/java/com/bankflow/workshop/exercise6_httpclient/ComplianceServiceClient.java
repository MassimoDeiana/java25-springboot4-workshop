package com.bankflow.workshop.exercise6_httpclient;

import com.bankflow.workshop.domain.ComplianceResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * SOLUTION: Exercise 6 (Bonus) — Declarative HTTP Service Client
 *
 * Replaces ComplianceRestClient with a declarative interface.
 * Spring generates the implementation via HttpServiceProxyFactory.
 */
@HttpExchange("/api/compliance")
public interface ComplianceServiceClient {

    @PostExchange("/checks")
    ComplianceResult validate(@RequestBody ComplianceRequest request);

    @GetExchange("/checks/{id}")
    ComplianceResult getResult(@PathVariable String id);

    @GetExchange("/health")
    String healthCheck();
}
