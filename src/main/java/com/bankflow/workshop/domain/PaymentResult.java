package com.bankflow.workshop.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResult(
        String paymentId,
        String status,
        String fromIban,
        String toIban,
        BigDecimal amount,
        Currency currency,
        Instant processedAt
) {}
