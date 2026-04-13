# Exercise 2 — Scoped Values

**Java 25 — JEP 487 (Finalized)**

## What changed?

`ScopedValue` replaces `ThreadLocal` for passing implicit context through a call chain. Unlike `ThreadLocal`, scoped values are:

- **Immutable** within a scope — no accidental overwrites
- **Bounded lifetime** — automatically cleaned up when scope ends
- **Virtual-thread friendly** — no per-thread storage overhead
- **Fail-fast** — throws if read outside a binding scope

## Before

```java
// AuditContext.java — ThreadLocal approach
public class AuditContext {
    private static final ThreadLocal<AuditInfo> CURRENT = new ThreadLocal<>();

    public static void set(AuditInfo info)  { CURRENT.set(info); }   // anyone can overwrite!
    public static AuditInfo get()           { return CURRENT.get(); } // returns null silently
    public static void clear()              { CURRENT.remove(); }     // easy to forget → leak!
}

// AuditFilter.java — manual try/finally
public void doFilter(AuditInfo auditInfo, Runnable requestHandler) {
    try {
        AuditContext.set(auditInfo);
        requestHandler.run();
    } finally {
        AuditContext.clear();  // forget this = memory leak
    }
}

// PaymentAuditService.java — reading deep in call chain
public void logPaymentEvent(String paymentId, String action) {
    AuditInfo info = AuditContext.get();  // might be null
    String user = (info != null) ? info.userId() : "AUDIT MISSING";
    // ...
}
```

## After

```java
// AuditContext.java — ScopedValue approach
public class AuditContext {
    public static final ScopedValue<AuditInfo> CURRENT = ScopedValue.newInstance();
    // No set(), no clear() — they don't exist!
}

// AuditFilter.java — automatic cleanup
public void doFilter(AuditInfo auditInfo, Runnable requestHandler) {
    ScopedValue.where(AuditContext.CURRENT, auditInfo)
               .run(requestHandler);
    // Auto-cleaned when run() returns — no try/finally, no leak
}

// PaymentAuditService.java — fail-fast
public void logPaymentEvent(String paymentId, String action) {
    AuditInfo info = AuditContext.CURRENT.get();  // throws if not bound!
    String user = info.userId();                  // always safe
    // ...
}
```

## Key takeaways

| ThreadLocal | ScopedValue |
|-------------|-------------|
| Mutable — `set()` anytime | Immutable within scope |
| Returns `null` if not set | Throws `NoSuchElementException` |
| Manual `clear()` in `finally` | Automatic cleanup |
| Memory leak risk | No leak possible |
| Heavy with virtual threads | Designed for virtual threads |
