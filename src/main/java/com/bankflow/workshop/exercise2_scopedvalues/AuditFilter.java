package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;

/**
 * SOLUTION: Exercise 2 — Scoped Values (JEP 487)
 *
 * ScopedValue.where().run() replaces the try/finally pattern.
 * Automatic cleanup when run() returns — no leak possible.
 */
public class AuditFilter {

    public void doFilter(AuditInfo auditInfo, Runnable requestHandler) {
        ScopedValue.where(AuditContext.CURRENT, auditInfo)
                   .run(requestHandler);
    }
}
