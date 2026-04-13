# Exercise 3 — Stable Values (Lazy Initialization)

## ⏱ Estimated time: 10 minutes

## 📖 What's new?

**Stable Values** (JEP 502, preview in Java 25) provide a safe, JVM-optimized way to lazily
initialize constants. Think of it as `final` but with deferred assignment.

- Set at most **once** — then treated as a constant by the JVM
- **Thread-safe** by design — no `synchronized`, no `volatile`, no double-checked locking
- JVM can **constant-fold** the value, just like a `final` field

## 🎯 Your mission

Look at `EncryptionKeyProvider.java` — it lazily initializes an encryption key using the
classic double-checked locking pattern. This is verbose, error-prone, and the JVM can't
optimize the `volatile` field.

**Refactor to use `StableValue`:**

1. Replace `volatile SecretKey encryptionKey` with `StableValue<SecretKey>`
2. Replace the double-checked locking in `getEncryptionKey()` with `stableValue.orElseSet(...)`
3. Do the same for the `RegulationConfig` in `RegulationConfigLoader.java`

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise3Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — Declaring a StableValue</summary>

```java
private final StableValue<MyType> value = StableValue.of();
```
</details>

<details>
<summary>Hint 2 — Lazy initialization</summary>

```java
public MyType getValue() {
    return value.orElseSet(() -> expensiveComputation());
}
// Thread-safe, set once, then constant-folded by JVM
```
</details>
