package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;

/**
 * TODO: Exercise 2 — Scoped Values
 *
 * CURRENT STATE (before):
 * - Uses try/finally to set and clear the ThreadLocal
 * - If clear() is forgotten (or exception path misses it) → memory leak
 *
 * YOUR TASK (after):
 * - Replace the try/finally with ScopedValue.where(...).run(...)
 * - The scope automatically cleans up — no finally needed!
 */
public class AuditFilter {

    /**
     * Simulates a servlet filter that sets audit context for the request.
     * In a real app, this would be a jakarta.servlet.Filter.
     */
    public void doFilter(AuditInfo auditInfo, Runnable requestHandler) {
        // TODO: Replace with ScopedValue.where(AuditContext.current(), auditInfo).run(requestHandler)
        try {
            AuditContext.set(auditInfo);
            requestHandler.run();
        } finally {
            AuditContext.clear(); // Easy to forget! Memory leak if missed.
        }
    }
}
