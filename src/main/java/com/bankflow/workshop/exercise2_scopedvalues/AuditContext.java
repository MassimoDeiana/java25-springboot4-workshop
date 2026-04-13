package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;

/**
 * TODO: Exercise 2 — Scoped Values
 *
 * CURRENT STATE (before):
 * - Uses ThreadLocal to store audit context
 * - Mutable: any code can call set() and overwrite the value
 * - Must remember to call clear() — forgetting causes memory leaks
 * - Not safe with virtual threads (each virtual thread gets its own copy = expensive)
 *
 * YOUR TASK (after):
 * 1. Replace ThreadLocal<AuditInfo> with ScopedValue<AuditInfo>
 * 2. Remove the set() method — ScopedValue is bound externally via ScopedValue.where()
 * 3. Remove the clear() method — ScopedValue auto-cleans when scope ends
 * 4. Keep a way to access the current value (get or expose the ScopedValue)
 */
public class AuditContext {

    // TODO: Replace with ScopedValue<AuditInfo>
    private static final ThreadLocal<AuditInfo> CURRENT = new ThreadLocal<>();

    /**
     * Set the audit info for the current thread.
     * Problem: any code can call this and overwrite the value!
     */
    public static void set(AuditInfo info) {
        CURRENT.set(info);
    }

    /**
     * Get the audit info for the current thread.
     * Problem: returns null if not set — no fail-fast behavior.
     */
    public static AuditInfo get() {
        return CURRENT.get();
    }

    /**
     * Clear the audit info — MUST be called to avoid memory leaks!
     * Problem: easy to forget in error paths.
     */
    public static void clear() {
        CURRENT.remove();
    }
}
