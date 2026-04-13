# Exercise 2 — Scoped Values (Replace ThreadLocal)

## ⏱ Estimated time: 15 minutes

## 📖 What's new?

**Scoped Values** (JEP 487, finalized in Java 25) provide a modern alternative to `ThreadLocal`.

Key differences:
- **Immutable** — once bound, the value cannot be changed within the scope
- **Bounded lifetime** — automatically cleaned up when the scope ends (no memory leaks!)
- **Virtual-thread friendly** — designed to work efficiently with Project Loom
- **Fail-fast** — throws if accessed outside its scope

## 🎯 Your mission

Look at `AuditContext.java` — it uses `ThreadLocal` to propagate audit information
(userId, correlationId) through the call chain. The `AuditFilter` sets it, and
`PaymentAuditService` reads it deep in the call chain.

**Problems with the current code:**
- `AuditContext.clear()` can be forgotten → memory leak
- The value is mutable → any code can overwrite it
- Not safe with virtual threads

**Refactor to use `ScopedValue`:**

1. In `AuditContext.java`: Replace `ThreadLocal<AuditInfo>` with `ScopedValue<AuditInfo>`
2. In `AuditFilter.java`: Replace `set/clear` with `ScopedValue.where(...).run(...)`
3. In `PaymentAuditService.java`: Use `ScopedValue.get()` to read the value
4. Remove the `clear()` method — it's no longer needed!

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise2Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — Declaring a ScopedValue</summary>

```java
private static final ScopedValue<MyType> CURRENT = ScopedValue.newInstance();
```
</details>

<details>
<summary>Hint 2 — Binding and running in scope</summary>

```java
ScopedValue.where(MY_SCOPED_VALUE, someValue)
    .run(() -> {
        // value is available here and in all called methods
        doSomething();
    });
```
</details>

<details>
<summary>Hint 3 — Reading the value</summary>

```java
// Returns the value or throws if not bound
MyType value = MY_SCOPED_VALUE.get();

// Check if bound first
if (MY_SCOPED_VALUE.isBound()) {
    MyType value = MY_SCOPED_VALUE.get();
}
```
</details>
