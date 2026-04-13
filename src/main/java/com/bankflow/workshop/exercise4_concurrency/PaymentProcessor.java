package com.bankflow.workshop.exercise4_concurrency;

import com.bankflow.workshop.domain.*;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * SOLUTION: Exercise 4 — Structured Concurrency (JEP 505)
 *
 * StructuredTaskScope replaces CompletableFuture.
 * If any task fails, siblings are cancelled automatically.
 */
public class PaymentProcessor {

    private final BankingServices services;

    public PaymentProcessor(BankingServices services) {
        this.services = services;
    }

    public PaymentResult processPayment(PaymentRequest request) {
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {

            Subtask<BankingServices.Balance> balance = scope.fork(() ->
                    services.checkBalance(request.fromIban()));
            Subtask<ComplianceResult> compliance = scope.fork(() ->
                    services.validateCompliance(request));
            Subtask<CustomerProfile> profile = scope.fork(() ->
                    services.fetchCustomerProfile(request.fromIban()));

            scope.join();

            // All succeeded — safe to read results
            if (balance.get().available().compareTo(request.amount()) < 0) {
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
        } catch (StructuredTaskScope.FailedException e) {
            if (e.getCause() instanceof BankingServices.ComplianceException) {
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
            throw new RuntimeException("Payment processing failed", e.getCause());
        }
    }
}
