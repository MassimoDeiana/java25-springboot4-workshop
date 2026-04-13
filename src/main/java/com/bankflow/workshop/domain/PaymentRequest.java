package com.bankflow.workshop.domain;

import java.math.BigDecimal;

public record PaymentRequest(
        String fromIban,
        String toIban,
        BigDecimal amount,
        Currency currency
) {}
