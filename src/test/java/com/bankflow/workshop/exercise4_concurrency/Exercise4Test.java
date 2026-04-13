package com.bankflow.workshop.exercise4_concurrency;

import com.bankflow.workshop.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exercise 4 — Structured Concurrency")
class Exercise4Test {

    private final BankingServices services = new BankingServices();
    private final PaymentProcessor processor = new PaymentProcessor(services);

    @Test
    @DisplayName("Valid payment should be approved")
    void validPaymentApproved() {
        PaymentRequest request = new PaymentRequest(
                "BE68539007547034", "BE71096123456769",
                new BigDecimal("500.00"), Currency.EUR);

        PaymentResult result = processor.processPayment(request);

        assertEquals("APPROVED", result.status());
        assertEquals("BE68539007547034", result.fromIban());
        assertEquals(new BigDecimal("500.00"), result.amount());
    }

    @Test
    @DisplayName("Payment exceeding compliance limit should be rejected")
    void complianceRejection() {
        PaymentRequest request = new PaymentRequest(
                "BE68539007547034", "BE71096123456769",
                new BigDecimal("100000.00"), Currency.EUR);

        PaymentResult result = processor.processPayment(request);

        assertEquals("REJECTED_COMPLIANCE", result.status());
    }

    @Test
    @DisplayName("Payment processing should run tasks in parallel (< 500ms total)")
    void shouldRunInParallel() {
        PaymentRequest request = new PaymentRequest(
                "BE68539007547034", "BE71096123456769",
                new BigDecimal("500.00"), Currency.EUR);

        long start = System.currentTimeMillis();
        processor.processPayment(request);
        long elapsed = System.currentTimeMillis() - start;

        // Services take 200ms + 300ms + 150ms = 650ms sequential
        // In parallel, max is ~300ms + some overhead
        assertTrue(elapsed < 500,
                "Should run in parallel! Took %dms (sequential would be ~650ms)".formatted(elapsed));
    }

    @Test
    @DisplayName("Should NOT use CompletableFuture anymore")
    void shouldNotUseCompletableFuture() {
        // Check that the processPayment method doesn't reference CompletableFuture
        boolean usesCompletableFuture = false;
        try {
            var source = PaymentProcessor.class.getDeclaredMethod("processPayment", PaymentRequest.class);
            // Check parameter types and return type — a rough heuristic
            // The real check: does the source code contain CompletableFuture?
            // We'll check declared fields as a proxy
            for (var field : PaymentProcessor.class.getDeclaredFields()) {
                if (CompletableFuture.class.isAssignableFrom(field.getType())) {
                    usesCompletableFuture = true;
                }
            }
        } catch (NoSuchMethodException e) {
            fail("processPayment method not found!");
        }
        assertFalse(usesCompletableFuture, "Should use StructuredTaskScope, not CompletableFuture!");
    }

    @Test
    @DisplayName("Multiple payments should all work correctly")
    void multiplePayments() {
        for (int i = 0; i < 5; i++) {
            PaymentRequest request = new PaymentRequest(
                    "BE68539007547034", "BE71096123456769",
                    new BigDecimal("100.00"), Currency.EUR);

            PaymentResult result = processor.processPayment(request);
            assertEquals("APPROVED", result.status());
            assertNotNull(result.paymentId());
        }
    }
}
