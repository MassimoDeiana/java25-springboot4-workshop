# Exercise 1 — Flexible Constructor Bodies

## ⏱ Estimated time: 10 minutes

## 📖 What's new?

In Java 25 (JEP 513), you can now execute code **before** calling `super()` or `this()` in a constructor.
Previously, the first statement in a constructor HAD to be `super()` or `this()`.

This means you can:
- Validate arguments before passing them to the parent
- Log or transform data before construction
- Fail fast with a clear error instead of half-initializing an object

## 🎯 Your mission

Look at `WireTransfer.java` — it extends `Transaction` and needs to validate the IBAN and amount
before calling `super()`. Currently it uses an **ugly static helper** method to work around the old restriction.

**Refactor it to:**
1. Move the IBAN validation **before** the `super()` call — inline, no static helper
2. Move the amount validation **before** the `super()` call
3. Delete the static helper method `validateIban()`
4. Add a currency validation (currency must not be null)

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise1Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — Basic syntax</summary>

```java
public Child(String value) {
    // This is now legal in Java 25!
    if (value == null) {
        throw new IllegalArgumentException("value required");
    }
    super(value);
}
```
</details>

<details>
<summary>Hint 2 — You can also do computations</summary>

```java
public Child(String rawInput) {
    String cleaned = rawInput.strip().toUpperCase();
    if (cleaned.isEmpty()) {
        throw new IllegalArgumentException("empty input");
    }
    super(cleaned);  // pass the transformed value
}
```
</details>
