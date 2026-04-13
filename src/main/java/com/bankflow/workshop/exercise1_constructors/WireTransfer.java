package com.bankflow.workshop.exercise1_constructors;

import com.bankflow.workshop.domain.Currency;
import com.bankflow.workshop.domain.IbanValidator;

import java.math.BigDecimal;

/**
 * TODO: Exercise 1 — Flexible Constructor Bodies
 *
 * CURRENT STATE (before):
 * - Uses a static helper method `validateIban()` to work around the
 *   restriction that super() must be the first statement.
 * - Amount validation happens AFTER super(), meaning the parent Transaction
 *   is already half-initialized with a potentially invalid amount.
 * - Currency is not validated at all.
 *
 * YOUR TASK (after):
 * 1. Move IBAN validation BEFORE the super() call — inline, no static helper.
 * 2. Move amount validation BEFORE the super() call.
 * 3. Add currency validation (must not be null) BEFORE super().
 * 4. Delete the static helper method validateIban().
 *
 * The result: the parent Transaction is NEVER constructed with invalid data.
 */
public class WireTransfer extends Transaction {

    private final Currency currency;
    private final String beneficiaryName;

    public WireTransfer(String iban, BigDecimal amount, Currency currency, String beneficiaryName) {
        // Problem: must call super() first — forced to use static helper
        super(validateIban(iban), amount);

        // Problem: this runs AFTER super() — parent already initialized with bad amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive, got: " + amount);
        }

        // Problem: currency is not validated at all
        this.currency = currency;
        this.beneficiaryName = beneficiaryName;
    }

    // Ugly workaround — static helper just to validate before super()
    private static String validateIban(String iban) {
        if (!IbanValidator.isValid(iban)) {
            throw new IllegalArgumentException("Invalid IBAN: " + iban);
        }
        return iban;
    }

    public Currency getCurrency() { return currency; }
    public String getBeneficiaryName() { return beneficiaryName; }

    @Override
    public String toString() {
        return "WireTransfer{id=%s, iban=%s, amount=%s %s, beneficiary=%s}"
                .formatted(getTransactionId(), getIban(), getAmount(), currency, beneficiaryName);
    }
}
