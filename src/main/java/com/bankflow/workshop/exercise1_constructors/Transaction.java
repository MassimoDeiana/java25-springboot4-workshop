package com.bankflow.workshop.exercise1_constructors;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all banking transactions.
 * DO NOT MODIFY this class — it represents a legacy base class you can't change.
 */
public abstract class Transaction {

    private final String transactionId;
    private final String iban;
    private final BigDecimal amount;
    private final Instant createdAt;

    protected Transaction(String iban, BigDecimal amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.iban = iban;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    public String getTransactionId() { return transactionId; }
    public String getIban() { return iban; }
    public BigDecimal getAmount() { return amount; }
    public Instant getCreatedAt() { return createdAt; }
}
