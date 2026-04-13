package com.bankflow.workshop.exercise5_nullsafety;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
