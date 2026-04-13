# Exercise 4 — Structured Concurrency

**Java 25 — JEP 505 (Preview)**

## What changed?

Structured Concurrency treats a group of concurrent tasks as a **single unit of work**. If one fails, the others are automatically cancelled. No more orphaned threads or forgotten futures.

Two built-in policies:
- `ShutdownOnFailure` — fail-fast: cancel everything if any task fails
- `ShutdownOnSuccess` — race: cancel the rest when the first succeeds

## Before

```java
public PaymentResult processPayment(PaymentRequest request) {
    // Launch 3 tasks in parallel
    var balanceFuture    = CompletableFuture.supplyAsync(() ->
            bankingServices.checkBalance(request.fromIban()));
    var complianceFuture = CompletableFuture.supplyAsync(() ->
            bankingServices.validateCompliance(request));
    var profileFuture    = CompletableFuture.supplyAsync(() ->
            bankingServices.fetchCustomerProfile(request.fromIban()));

    try {
        var balance    = balanceFuture.get();     // blocks
        var compliance = complianceFuture.get();  // blocks
        var profile    = profileFuture.get();     // blocks
        // ... build result
    } catch (ExecutionException e) {
        // If compliance fails, balance & profile futures KEEP RUNNING!
        // Wasted resources, possible side effects
        throw new RuntimeException(e.getCause());
    }
}
```

## After

```java
public PaymentResult processPayment(PaymentRequest request)
        throws InterruptedException, ExecutionException {

    try (var scope = StructuredTaskScope.open(
            Joiner.awaitAllSuccessfulOrThrow())) {

        // Fork subtasks — they're children of this scope
        var balance    = scope.fork(() ->
                bankingServices.checkBalance(request.fromIban()));
        var compliance = scope.fork(() ->
                bankingServices.validateCompliance(request));
        var profile    = scope.fork(() ->
                bankingServices.fetchCustomerProfile(request.fromIban()));

        scope.join();  // wait for all or fail-fast

        // All succeeded — safe to read results
        var b = balance.get();
        var c = compliance.get();
        var p = profile.get();
        // ... build result
    }
    // Scope closes: any still-running subtasks are cancelled + joined
}
```

## Key takeaways

| CompletableFuture | Structured Concurrency |
|-------------------|----------------------|
| Tasks are independent | Tasks are scoped together |
| Failure leaves others running | Failure cancels siblings |
| Manual error unwrapping | Automatic propagation |
| Thread dumps show unrelated threads | Thread dumps show parent-child |
| try/catch with ExecutionException | try-with-resources, auto-cleanup |
