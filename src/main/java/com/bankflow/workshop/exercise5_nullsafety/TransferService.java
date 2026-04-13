package com.bankflow.workshop.exercise5_nullsafety;

import com.bankflow.workshop.domain.Account;

import java.math.BigDecimal;

/**
 * TODO: Exercise 5 — JSpecify Null Safety
 *
 * CURRENT STATE (before):
 * - Calls findByIban() without null-checking the result
 * - Will throw NullPointerException at runtime if account not found
 * - No compile-time warning because the null contract is not expressed
 *
 * YOUR TASK (after):
 * - After annotating AccountLookupService with @Nullable, the IDE will flag this code
 * - Add proper null checks and throw AccountNotFoundException
 * - The code should have ZERO null-safety warnings in IntelliJ
 */
public class TransferService {

    private final AccountLookupService lookupService;

    public TransferService(AccountLookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * Execute a transfer between two accounts.
     *
     * BUG: This will throw NullPointerException if either account is not found!
     * After adding @Nullable annotations to AccountLookupService, fix this method.
     */
    public TransferResult executeTransfer(String fromIban, String toIban, BigDecimal amount) {
        // TODO: After @Nullable annotations, the IDE will warn here
        // Fix by adding null checks and throwing AccountNotFoundException
        Account from = lookupService.findByIban(fromIban);
        Account to = lookupService.findByIban(toIban);

        // BUG: NPE if 'from' or 'to' is null!
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
