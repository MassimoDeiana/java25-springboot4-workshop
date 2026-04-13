# Exercise 4 — Structured Concurrency

## ⏱ Estimated time: 20 minutes

## 📖 What's new?

**Structured Concurrency** (JEP 505, preview in Java 25) treats groups of concurrent tasks
as a single unit of work. When one subtask fails, the others are automatically cancelled.

Two built-in policies:
- `ShutdownOnFailure` — cancel all if ANY fails (fail-fast)
- `ShutdownOnSuccess` — cancel all once the FIRST succeeds (race pattern)

Key benefits:
- **Automatic cancellation** — no orphaned threads doing useless work
- **Clean error handling** — `throwIfFailed()` propagates the first error
- **Thread dump clarity** — parent-child relationships are visible
- **Resource safety** — try-with-resources guarantees cleanup

## 🎯 Your mission

Look at `PaymentProcessor.java` — it processes a payment by calling three services
in parallel using `CompletableFuture`. If the compliance check fails, the other
futures keep running (wasted resources!).

**Refactor to use `StructuredTaskScope`:**

1. Replace the three `CompletableFuture.supplyAsync()` calls with `scope.fork()`
2. Use `StructuredTaskScope.ShutdownOnFailure` as the scope
3. Use `scope.join()` and `scope.throwIfFailed()` instead of `.get()` on each future
4. Wrap everything in a try-with-resources block

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise4Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — Basic structure</summary>

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<TypeA> a = scope.fork(() -> callServiceA());
    Subtask<TypeB> b = scope.fork(() -> callServiceB());

    scope.join();          // wait for all
    scope.throwIfFailed(); // propagate first error

    // All subtasks completed successfully
    TypeA resultA = a.get();
    TypeB resultB = b.get();
}
```
</details>

<details>
<summary>Hint 2 — Exception handling</summary>

```java
// throwIfFailed() wraps the first subtask exception in an ExecutionException.
// You can also use throwIfFailed(Function) to map to your own exception type:
scope.throwIfFailed(e -> new PaymentProcessingException("Payment failed", e));
```
</details>

<details>
<summary>Hint 3 — Method signature</summary>

The method should declare `throws InterruptedException` since `scope.join()` can be interrupted.
You can also catch and wrap it if you prefer unchecked exceptions.
</details>
