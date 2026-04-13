package com.bankflow.workshop.exercise5_nullsafety;

import com.bankflow.workshop.domain.Account;

import java.math.BigDecimal;

/**
 * SOLUTION: Exercise 5 — JSpecify Null Safety
 *
 * Proper null checks after @Nullable annotations.
 * Throws AccountNotFoundException instead of NPE.
 */
public class TransferService {

    private final AccountLookupService lookupService;

    public TransferService(AccountLookupService lookupService) {
        this.lookupService = lookupService;
    }

    public TransferResult executeTransfer(String fromIban, String toIban, BigDecimal amount) {
        Account from = lookupService.findByIban(fromIban);
        Account to = lookupService.findByIban(toIban);

        if (from == null) {
            throw new AccountNotFoundException("Account not found: " + fromIban);
        }
        if (to == null) {
            throw new AccountNotFoundException("Account not found: " + toIban);
        }

        if (from.getBalance().compareTo(amount) < 0) {
            return new TransferResult(false, "Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        return new TransferResult(true,
                "Transfer of %s from %s to %s completed".formatted(amount, fromIban, toIban));
    }

    public record TransferResult(boolean success, String message) {}
}
