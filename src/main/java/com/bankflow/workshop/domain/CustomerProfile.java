package com.bankflow.workshop.domain;

public record CustomerProfile(
        String customerId,
        String name,
        String email,
        String riskCategory,
        boolean sanctioned
) {}
