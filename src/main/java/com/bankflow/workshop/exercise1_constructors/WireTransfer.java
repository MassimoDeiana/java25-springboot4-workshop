package com.bankflow.workshop.exercise1_constructors;

import com.bankflow.workshop.domain.Currency;
import com.bankflow.workshop.domain.IbanValidator;

import java.math.BigDecimal;

/**
 * SOLUTION: Exercise 1 — Flexible Constructor Bodies (JEP 513)
 *
 * All validation happens BEFORE super() — the parent Transaction
 * is never constructed with invalid data. No static helper needed.
 */
public class WireTransfer extends Transaction {

    private final Currency currency;
    private final String beneficiaryName;

    public WireTransfer(String iban, BigDecimal amount, Currency currency, String beneficiaryName) {
        // Validate BEFORE super() — Java 25 allows this!
        if (!IbanValidator.isValid(iban)) {
            throw new IllegalArgumentException("Invalid IBAN: " + iban);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive, got: " + amount);
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        super(iban, amount);

        this.currency = currency;
        this.beneficiaryName = beneficiaryName;
    }

    public Currency getCurrency() { return currency; }
    public String getBeneficiaryName() { return beneficiaryName; }

    @Override
    public String toString() {
        return "WireTransfer{id=%s, iban=%s, amount=%s %s, beneficiary=%s}"
                .formatted(getTransactionId(), getIban(), getAmount(), currency, beneficiaryName);
    }
}
