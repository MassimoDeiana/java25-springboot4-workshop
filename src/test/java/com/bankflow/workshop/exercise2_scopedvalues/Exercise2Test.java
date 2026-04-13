package com.bankflow.workshop.exercise2_scopedvalues;

import com.bankflow.workshop.domain.AuditInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exercise 2 — Scoped Values")
class Exercise2Test {

    private final AuditFilter filter = new AuditFilter();
    private final PaymentAuditService auditService = new PaymentAuditService();

    @Test
    @DisplayName("Audit info should be accessible within the scope")
    void auditInfoAccessibleInScope() {
        AuditInfo info = new AuditInfo("user-123", "corr-456", "192.168.1.1");
        AtomicReference<String> result = new AtomicReference<>();

        filter.doFilter(info, () -> {
            result.set(auditService.logPaymentEvent("PAY-001", "CREATED"));
        });

        String log = result.get();
        assertNotNull(log);
        assertTrue(log.contains("user-123"), "Should contain userId");
        assertTrue(log.contains("corr-456"), "Should contain correlationId");
        assertTrue(log.contains("PAY-001"), "Should contain paymentId");
        assertTrue(log.contains("CREATED"), "Should contain action");
        assertFalse(log.contains("MISSING"), "Should NOT say audit is missing");
    }

    @Test
    @DisplayName("Audit info should NOT be accessible outside the scope")
    void auditInfoNotAccessibleOutsideScope() {
        AuditInfo info = new AuditInfo("user-123", "corr-456", "192.168.1.1");

        // Run a scoped operation
        filter.doFilter(info, () -> {
            // Value is available here
            auditService.logPaymentEvent("PAY-001", "CREATED");
        });

        // After scope ends, accessing the value should either:
        // - throw (ScopedValue behavior — preferred!)
        // - or return a "missing" message (ThreadLocal behavior)
        // The test accepts both, but the ScopedValue version is cleaner
        try {
            String result = auditService.logPaymentEvent("PAY-002", "OUTSIDE_SCOPE");
            // If we get here with ThreadLocal, it should say MISSING
            assertTrue(result.contains("MISSING") || result.contains("no context"),
                    "Outside scope, audit should not be available");
        } catch (Exception e) {
            // ScopedValue throws NoSuchElementException — this is the BETTER behavior
            assertTrue(e instanceof java.util.NoSuchElementException,
                    "ScopedValue should throw NoSuchElementException when not bound");
        }
    }

    @Test
    @DisplayName("AuditContext should use ScopedValue, not ThreadLocal")
    void shouldUseScopedValue() {
        // Check that AuditContext no longer uses ThreadLocal
        boolean usesThreadLocal = false;
        for (Field field : AuditContext.class.getDeclaredFields()) {
            if (ThreadLocal.class.isAssignableFrom(field.getType())) {
                usesThreadLocal = true;
                break;
            }
        }
        assertFalse(usesThreadLocal,
                "AuditContext should use ScopedValue instead of ThreadLocal!");
    }

    @Test
    @DisplayName("AuditContext should not have a clear() method anymore")
    void shouldNotHaveClearMethod() {
        boolean hasClear;
        try {
            AuditContext.class.getDeclaredMethod("clear");
            hasClear = true;
        } catch (NoSuchMethodException e) {
            hasClear = false;
        }
        assertFalse(hasClear,
                "AuditContext should not have a clear() method — ScopedValue auto-cleans!");
    }

    @Test
    @DisplayName("AuditContext should not have a set() method anymore")
    void shouldNotHaveSetMethod() {
        boolean hasSet;
        try {
            AuditContext.class.getDeclaredMethod("set", AuditInfo.class);
            hasSet = true;
        } catch (NoSuchMethodException e) {
            hasSet = false;
        }
        assertFalse(hasSet,
                "AuditContext should not have a set() method — ScopedValue is bound via .where()!");
    }
}
