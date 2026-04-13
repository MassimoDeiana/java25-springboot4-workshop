package com.bankflow.workshop.domain;

import java.math.BigDecimal;

public class Account {

    private String id;
    private String iban;
    private String holderName;
    private BigDecimal balance;
    private Currency currency;

    public Account() {}

    public Account(String id, String iban, String holderName, BigDecimal balance, Currency currency) {
        this.id = id;
        this.iban = iban;
        this.holderName = holderName;
        this.balance = balance;
        this.currency = currency;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    @Override
    public String toString() {
        return "Account{id='%s', iban='%s', holder='%s', balance=%s %s}"
                .formatted(id, iban, holderName, balance, currency);
    }
}
