package com.bankflow.workshop.exercise5_nullsafety;

import com.bankflow.workshop.domain.Account;
import com.bankflow.workshop.domain.Currency;

import java.math.BigDecimal;
import java.util.Map;

/**
 * TODO: Exercise 7 — JSpecify Null Safety
 *
 * CURRENT STATE (before):
 * - findByIban() returns null if not found, but the signature doesn't say so!
 * - findByCustomerId() also returns null silently
 * - Callers have no idea they need to null-check
 *
 * YOUR TASK (after):
 * 1. Annotate methods that can return null with @Nullable on the return type
 * 2. Parameters that must NOT be null stay unannotated (under @NullMarked, they're non-null by default)
 * 3. Create a package-info.java with @NullMarked for this package
 */
public class AccountLookupService {

    // Simulated database
    private static final Map<String, Account> ACCOUNTS = Map.of(
            "BE68539007547034", new Account("ACC001", "BE68539007547034", "Alice Dupont",
                    new BigDecimal("10250.75"), Currency.EUR),
            "BE71096123456769", new Account("ACC002", "BE71096123456769", "Bob Martin",
                    new BigDecimal("5430.20"), Currency.EUR)
    );

    /**
     * Find an account by IBAN.
     * Returns null if the account doesn't exist — but the signature doesn't tell you!
     *
     * TODO: Add @Nullable to the return type
     */
    public Account findByIban(String iban) {
        return ACCOUNTS.get(iban);
    }

    /**
     * Find an account by customer ID.
     * Also returns null silently.
     *
     * TODO: Add @Nullable to the return type
     */
    public Account findByCustomerId(String customerId) {
        return ACCOUNTS.values().stream()
                .filter(a -> a.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
}
