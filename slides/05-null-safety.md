# Exercise 5 — JSpecify Null Safety

**Spring Boot 4 — JSpecify integration**

## What changed?

Spring Boot 4 embraces [JSpecify](https://jspecify.dev/) for null-safety annotations. The key idea:

- `@NullMarked` on a package = everything is **non-null by default**
- `@Nullable` explicitly marks the exceptions (things that can be null)
- IDEs and static analysis tools flag violations — bugs caught at compile time, not production

## Before

```java
// No package-level null contract — anything could be null

public class AccountLookupService {

    // Returns null but nothing tells the caller!
    public Account findByIban(String iban) {
        return accounts.stream()
                .filter(a -> a.getIban().equals(iban))
                .findFirst()
                .orElse(null);  // silent null
    }
}

public class TransferService {

    public PaymentResult executeTransfer(String fromIban, String toIban, BigDecimal amount) {
        Account from = lookupService.findByIban(fromIban);
        Account to   = lookupService.findByIban(toIban);

        // BUG: no null check → NullPointerException in production!
        if (from.getBalance().compareTo(amount) < 0) { ... }
    }
}
```

## After

```java
// package-info.java — everything in this package is non-null by default
@NullMarked
package com.bankflow.workshop.exercise5_nullsafety;

import org.jspecify.annotations.NullMarked;
```

```java
public class AccountLookupService {

    // @Nullable makes the contract explicit — caller MUST handle null
    public @Nullable Account findByIban(String iban) {
        return accounts.stream()
                .filter(a -> a.getIban().equals(iban))
                .findFirst()
                .orElse(null);
    }
}

public class TransferService {

    public PaymentResult executeTransfer(String fromIban, String toIban, BigDecimal amount) {
        Account from = lookupService.findByIban(fromIban);
        Account to   = lookupService.findByIban(toIban);

        // IDE warns: "from" might be null — forces you to handle it
        if (from == null) throw new AccountNotFoundException("Account not found: " + fromIban);
        if (to == null)   throw new AccountNotFoundException("Account not found: " + toIban);

        if (from.getBalance().compareTo(amount) < 0) { ... }
    }
}
```

## Key takeaways

| Before | After |
|--------|-------|
| Nullability is implicit, undocumented | `@NullMarked` makes non-null the default |
| `null` returns are invisible to callers | `@Nullable` makes them explicit |
| NullPointerException at runtime | IDE/compiler warning at dev time |
| Defensive null checks everywhere | Null checks only where `@Nullable` |
