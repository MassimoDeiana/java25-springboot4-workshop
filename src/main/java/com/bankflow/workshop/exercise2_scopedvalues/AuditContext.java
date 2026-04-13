package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;

/**
 * SOLUTION: Exercise 2 — Scoped Values (JEP 487)
 *
 * Replaced ThreadLocal with ScopedValue.
 * No set(), no clear() — immutable, auto-cleaned, fail-fast.
 */
public class AuditContext {

    public static final ScopedValue<AuditInfo> CURRENT = ScopedValue.newInstance();

    public static AuditInfo get() {
        return CURRENT.get(); // throws NoSuchElementException if not bound
    }
}
