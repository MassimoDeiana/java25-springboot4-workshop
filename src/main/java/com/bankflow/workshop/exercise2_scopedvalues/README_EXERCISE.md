# Exercise 2 — Scoped Values (Replace ThreadLocal)

## Estimated time: 15 minutes

## Context

See the **virtual threads visual presentation** for the full picture on why `ScopedValue` exists and how it relates to virtual threads and structured concurrency.

Quick summary: `ThreadLocal` stores a **copy per thread**. With millions of virtual threads, that's millions of copies. `ScopedValue` is **shared, not copied** — child threads read the parent's binding directly, with zero overhead and automatic cleanup.

## Your mission

Look at `AuditContext.java` — it uses `ThreadLocal` to propagate audit information
(userId, correlationId) through the call chain. The `AuditFilter` sets it, and
`PaymentAuditService` reads it deep in the call chain.

**Problems with the current code:**
- `AuditContext.clear()` can be forgotten — memory leak (catastrophic at virtual-thread scale)
- The value is mutable — any code can overwrite it
- Each virtual thread gets its own copy — massive memory waste

**Refactor to use `ScopedValue`:**

1. In `AuditContext.java`: Replace `ThreadLocal<AuditInfo>` with `ScopedValue<AuditInfo>`
2. In `AuditFilter.java`: Replace `set/clear` with `ScopedValue.where(...).run(...)`
3. In `PaymentAuditService.java`: Use `ScopedValue.get()` to read the value
4. Remove the `set()` and `clear()` methods — they don't exist on ScopedValue!

## Run the tests

```bash
mvn test -Dtest="Exercise2Test"
```

## Hints

<details>
<summary>Hint 1 — Declaring a ScopedValue</summary>

```java
public static final ScopedValue<MyType> CURRENT = ScopedValue.newInstance();
```
</details>

<details>
<summary>Hint 2 — Binding and running in scope</summary>

```java
ScopedValue.where(MY_SCOPED_VALUE, someValue)
    .run(() -> {
        // value is available here and in all called methods
        // child virtual threads forked here inherit the binding for free
        doSomething();
    });
```
</details>

<details>
<summary>Hint 3 — Reading the value</summary>

```java
// Returns the value or throws NoSuchElementException if not bound
MyType value = MY_SCOPED_VALUE.get();

// Check if bound first (optional)
if (MY_SCOPED_VALUE.isBound()) {
    MyType value = MY_SCOPED_VALUE.get();
}
```
</details>
