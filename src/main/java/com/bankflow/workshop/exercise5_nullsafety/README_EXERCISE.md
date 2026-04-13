# Exercise 5 — JSpecify Null Safety

## ⏱ Estimated time: 10 minutes

## 📖 What's new?

Spring Boot 4 adopts **JSpecify** annotations (`@Nullable`, `@NonNull`, `@NullMarked`)
across the entire portfolio. This makes null contracts **explicit** in method signatures.

Benefits:
- IDE warns you at compile time about potential `NullPointerException`
- Kotlin automatically maps `@Nullable` to `Type?`
- IntelliJ IDEA 2025.3+ provides full support with data-flow analysis
- No more guessing "can this return null?"

Key annotations:
- `@NullMarked` on a package/class = everything is non-null by default
- `@Nullable` on a specific type = this one CAN be null
- No annotation needed for non-null (it's the default under `@NullMarked`)

## 🎯 Your mission

Look at `AccountLookupService.java` — it has methods that can return `null`, but the
signatures don't tell you. Callers in `TransferService.java` don't handle null properly,
leading to potential `NullPointerException` at runtime.

**Add null safety annotations:**

1. Add `@NullMarked` to the `package-info.java` (create it if needed)
2. Annotate `AccountLookupService.findByIban()` return type as `@Nullable`
3. Fix `TransferService.executeTransfer()` to properly handle the nullable returns
4. Make the code pass the IDE null-safety analysis with zero warnings

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise5Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — Package-level @NullMarked</summary>

Create a file `package-info.java` in the exercise7 package:

```java
@NullMarked
package com.bankflow.workshop.exercise7_nullsafety;

import org.jspecify.annotations.NullMarked;
```
</details>

<details>
<summary>Hint 2 — Annotating a nullable return</summary>

```java
import org.jspecify.annotations.Nullable;

public @Nullable Account findByIban(String iban) {
    // May return null if not found
}
```
</details>

<details>
<summary>Hint 3 — Handling nullable in the caller</summary>

```java
Account from = lookupService.findByIban(fromIban);
if (from == null) {
    throw new AccountNotFoundException("Account not found: " + fromIban);
}
// Now 'from' is guaranteed non-null — IDE knows this!
from.debit(amount);
```
</details>
