package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;

/**
 * SOLUTION: Exercise 2 — Scoped Values (JEP 487)
 *
 * ScopedValue.get() throws NoSuchElementException if not bound — fail-fast!
 * No null check needed.
 */
public class PaymentAuditService {

    public String logPaymentEvent(String paymentId, String action) {
        AuditInfo audit = AuditContext.get(); // throws if not bound

        return "AUDIT [%s] user=%s correlationId=%s payment=%s action=%s".formatted(
                audit.timestamp(),
                audit.userId(),
                audit.correlationId(),
                paymentId,
                action
        );
    }
}
