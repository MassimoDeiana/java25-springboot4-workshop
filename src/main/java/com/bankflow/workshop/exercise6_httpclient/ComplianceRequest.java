package com.bankflow.workshop.exercise6_httpclient;

import java.math.BigDecimal;

public record ComplianceRequest(
        String fromIban,
        String toIban,
        BigDecimal amount,
        String currency
) {}
