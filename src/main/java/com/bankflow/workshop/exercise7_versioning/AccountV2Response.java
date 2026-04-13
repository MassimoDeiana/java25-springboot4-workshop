package com.bankflow.workshop.exercise7_versioning;

import com.bankflow.workshop.domain.Currency;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * V2 response — richer structure with currency, metadata, and nested holder info.
 */
public record AccountV2Response(
        String id,
        String iban,
        HolderInfo holder,
        MoneyAmount balance,
        Instant lastActivity
) {
    public record HolderInfo(String name, String email) {}
    public record MoneyAmount(BigDecimal value, Currency currency) {}
}
