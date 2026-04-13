# Exercise 3 — Stable Values

**Java 25 — JEP 502 (Preview)**

## What changed?

`StableValue` provides a safe, JVM-optimized way to lazily initialize values. Think of it as `final` but with deferred assignment:

- Set at most **once** — then treated as a constant by the JVM
- **Thread-safe** by design — no `synchronized`, no `volatile`, no double-checked locking
- JVM can **constant-fold** the value, just like a `final` field

## Before

```java
public class EncryptionKeyProvider {

    private volatile SecretKey encryptionKey;  // volatile required for visibility

    public SecretKey getEncryptionKey() {
        if (encryptionKey == null) {                    // first check (no lock)
            synchronized (this) {                       // acquire lock
                if (encryptionKey == null) {             // second check (with lock)
                    encryptionKey = generateKey();       // expensive init
                }
            }
        }
        return encryptionKey;
    }
}
```

12 lines of boilerplate. Easy to get wrong (forgotten `volatile`, wrong lock object). JVM cannot optimize `volatile` reads.

## After

```java
public class EncryptionKeyProvider {

    private final StableValue<SecretKey> encryptionKey = StableValue.of();

    public SecretKey getEncryptionKey() {
        return encryptionKey.orElseSet(this::generateKey);
    }
}
```

3 lines. Thread-safe by construction. JVM constant-folds after first set.

## Key takeaways

| Double-checked locking | StableValue |
|------------------------|-------------|
| `volatile` + `synchronized` | No synchronization primitives |
| 12 lines of boilerplate | 3 lines |
| Easy to get wrong | Impossible to misuse |
| JVM treats as volatile read | JVM treats as constant after set |
| Developer manages thread safety | Thread safety built in |
