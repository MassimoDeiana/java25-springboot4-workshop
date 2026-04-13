package com.bankflow.workshop.exercise4_concurrency;

import com.bankflow.workshop.domain.*;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * TODO: Exercise 4 — Structured Concurrency
 *
 * CURRENT STATE (before):
 * - Uses CompletableFuture.supplyAsync() for parallel service calls
 * - If compliance fails, balance and profile futures KEEP RUNNING (wasted resources!)
 * - Error handling is messy — have to unwrap ExecutionException
 * - No automatic cancellation of other tasks on failure
 * - Thread dumps don't show parent-child relationships
 *
 * YOUR TASK (after):
 * 1. Open a scope with StructuredTaskScope.open(Joiner.awaitAllSuccessfulOrThrow())
 * 2. Use scope.fork() for each service call
 * 3. Use scope.join() — throws FailedException on first failure
 * 4. Wrap in try-with-resources for automatic cleanup
 * 5. Catch FailedException to handle compliance rejections gracefully
 *
 * Benefits: If compliance check fails, balance and profile calls are
 * cancelled immediately. No wasted resources, clean error propagation.
 */
public class PaymentProcessor {

    private final BankingServices services;

    public PaymentProcessor(BankingServices services) {
        this.services = services;
    }

    /**
     * Process a payment by checking balance, compliance, and customer profile in parallel.
     */
    public PaymentResult processPayment(PaymentRequest request) {
        // TODO: Replace with StructuredTaskScope

        CompletableFuture<BankingServices.Balance> balanceFuture =
                CompletableFuture.supplyAsync(() ->
                        services.checkBalance(request.fromIban()));

        CompletableFuture<ComplianceResult> complianceFuture =
                CompletableFuture.supplyAsync(() ->
                        services.validateCompliance(request));

        CompletableFuture<CustomerProfile> profileFuture =
                CompletableFuture.supplyAsync(() ->
                        services.fetchCustomerProfile(request.fromIban()));

        try {
            // Problem: if compliance fails here, the other futures keep running!
            BankingServices.Balance balance = balanceFuture.get();
            ComplianceResult compliance = complianceFuture.get();
            CustomerProfile profile = profileFuture.get();

            // Validate balance is sufficient
            if (balance.available().compareTo(request.amount()) < 0) {
                return new PaymentResult(
                        UUID.randomUUID().toString(),
                        "REJECTED_INSUFFICIENT_FUNDS",
                        request.fromIban(),
                        request.toIban(),
                        request.amount(),
                        request.currency(),
                        Instant.now()
                );
            }

            return new PaymentResult(
                    UUID.randomUUID().toString(),
                    "APPROVED",
                    request.fromIban(),
                    request.toIban(),
                    request.amount(),
                    request.currency(),
                    Instant.now()
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Payment processing interrupted", e);
        } catch (ExecutionException e) {
            // Messy: have to unwrap the real exception
            Throwable cause = e.getCause();
            if (cause instanceof BankingServices.ComplianceException) {
                return new PaymentResult(
                        UUID.randomUUID().toString(),
                        "REJECTED_COMPLIANCE",
                        request.fromIban(),
                        request.toIban(),
                        request.amount(),
                        request.currency(),
                        Instant.now()
                );
            }
            throw new RuntimeException("Payment processing failed", cause);
        }
    }
}
