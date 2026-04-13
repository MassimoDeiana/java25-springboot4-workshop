package com.bankflow.workshop.exercise4_concurrency;

import com.bankflow.workshop.domain.*;

import java.math.BigDecimal;

/**
 * Simulated downstream services for the payment processing exercise.
 * These simulate real service calls with artificial delays.
 * DO NOT MODIFY this class.
 */
public class BankingServices {

    /**
     * Checks the account balance. Takes ~200ms.
     */
    public Balance checkBalance(String accountId) {
        simulateLatency(200);
        return new Balance(accountId, new BigDecimal("10000.00"), Currency.EUR);
    }

    /**
     * Validates compliance (AML, sanctions, limits). Takes ~300ms.
     * Fails if the amount exceeds 50,000.
     */
    public ComplianceResult validateCompliance(PaymentRequest request) {
        simulateLatency(300);
        if (request.amount().compareTo(new BigDecimal("50000")) > 0) {
            throw new ComplianceException("Amount exceeds regulatory limit: " + request.amount());
        }
        return new ComplianceResult("CHK-001", true, "Approved", ComplianceResult.RiskLevel.LOW);
    }

    /**
     * Fetches the customer profile. Takes ~150ms.
     */
    public CustomerProfile fetchCustomerProfile(String customerId) {
        simulateLatency(150);
        return new CustomerProfile(customerId, "Alice Dupont", "alice@bank.be", "STANDARD", false);
    }

    private void simulateLatency(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Service call interrupted", e);
        }
    }

    // --- Supporting types ---

    public record Balance(String accountId, BigDecimal available, Currency currency) {}

    public static class ComplianceException extends RuntimeException {
        public ComplianceException(String message) {
            super(message);
        }
    }
}
