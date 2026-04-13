package com.bankflow.workshop.exercise5_nullsafety;

import com.bankflow.workshop.domain.Account;
import com.bankflow.workshop.domain.Currency;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Map;

/**
 * SOLUTION: Exercise 5 — JSpecify Null Safety
 *
 * @Nullable on return types makes the null contract explicit.
 * Callers are forced to handle nulls — IDE warns if they don't.
 */
public class AccountLookupService {

    private static final Map<String, Account> ACCOUNTS = Map.of(
            "BE68539007547034", new Account("ACC001", "BE68539007547034", "Alice Dupont",
                    new BigDecimal("10250.75"), Currency.EUR),
            "BE71096123456769", new Account("ACC002", "BE71096123456769", "Bob Martin",
                    new BigDecimal("5430.20"), Currency.EUR)
    );

    public @Nullable Account findByIban(String iban) {
        return ACCOUNTS.get(iban);
    }

    public @Nullable Account findByCustomerId(String customerId) {
        return ACCOUNTS.values().stream()
                .filter(a -> a.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
}
