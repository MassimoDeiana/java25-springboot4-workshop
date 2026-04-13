package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;

/**
 * TODO: Exercise 2 — Scoped Values
 *
 * This service reads the audit context deep in the call chain.
 *
 * CURRENT STATE (before):
 * - Calls AuditContext.get() which returns null if not set
 * - Has to do null check — no fail-fast behavior
 *
 * YOUR TASK (after):
 * - Use the ScopedValue to get the audit info
 * - ScopedValue.get() throws NoSuchElementException if not bound — fail-fast!
 * - Optionally use isBound() for a safer check
 */
public class PaymentAuditService {

    /**
     * Log an audit event for a payment. Reads the audit context from the current scope.
     */
    public String logPaymentEvent(String paymentId, String action) {
        // TODO: Replace with ScopedValue access
        AuditInfo audit = AuditContext.get();

        if (audit == null) {
            // ThreadLocal returns null if not set — silent failure
            return "AUDIT MISSING — no context available for payment " + paymentId;
        }

        return "AUDIT [%s] user=%s correlationId=%s payment=%s action=%s".formatted(
                audit.timestamp(),
                audit.userId(),
                audit.correlationId(),
                paymentId,
                action
        );
    }
}
