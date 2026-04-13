# Exercise 1 — Flexible Constructor Bodies

**Java 25 — JEP 513 (Finalized)**

## What changed?

Before Java 25, you **could not** execute code before `super()` or `this()` in a constructor. This forced ugly workarounds like static helper methods just to validate arguments.

Java 25 lifts this restriction: you can now write statements before `super()`, as long as you don't access `this`.

## Before

```java
public class WireTransfer extends Transaction {

    // Ugly static helper just to work around the restriction
    private static String validateIban(String iban) {
        if (!IbanValidator.isValid(iban)) {
            throw new IllegalArgumentException("Invalid IBAN: " + iban);
        }
        return iban;
    }

    public WireTransfer(String iban, BigDecimal amount, String currency, String beneficiaryName) {
        super(validateIban(iban), amount);  // static helper to validate before super()

        // Amount validation happens AFTER super() — object already half-constructed!
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        // No currency validation at all
        this.currency = currency;
        this.beneficiaryName = beneficiaryName;
    }
}
```

## After

```java
public class WireTransfer extends Transaction {

    // No static helper needed!

    public WireTransfer(String iban, BigDecimal amount, String currency, String beneficiaryName) {
        // Validate BEFORE super() — inline, no tricks
        if (!IbanValidator.isValid(iban)) {
            throw new IllegalArgumentException("Invalid IBAN: " + iban);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        super(iban, amount);  // only called after all validations pass

        this.currency = currency;
        this.beneficiaryName = beneficiaryName;
    }
}
```

## Key takeaways

| Before | After |
|--------|-------|
| Static helper methods as workaround | Inline validation before `super()` |
| Validation split across helper + constructor | All validation in one place |
| Half-constructed objects on failure | Object never created if invalid |
| `validateIban()` static method | Deleted — no longer needed |
