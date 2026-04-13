package com.bankflow.workshop.exercise7_versioning;

import java.math.BigDecimal;

/**
 * V1 response — simple flat structure.
 */
public record AccountV1Response(
        String id,
        String iban,
        String holderName,
        BigDecimal balance
) {}
