# Exercise 4 — Structured Concurrency

## Estimated time: 20 minutes

## Context

See the **virtual threads visual presentation** for the full picture on structured concurrency — why unstructured `ExecutorService.submit()` leads to leaked threads, and how `StructuredTaskScope` fixes it with parent-child relationships and automatic cancellation.

Quick summary: `StructuredTaskScope` treats concurrent tasks as a **single unit of work**. Fork subtasks, join them all, and if one fails the others are automatically cancelled. The concurrent equivalent of try-with-resources.

## Your mission

Look at `PaymentProcessor.java` — it processes a payment by calling three services
in parallel using `CompletableFuture`. If the compliance check fails, the other
futures keep running (wasted resources!).

**Refactor to use `StructuredTaskScope`:**

1. Replace the three `CompletableFuture.supplyAsync()` calls with `scope.fork()`
2. Open a scope with `Joiner.awaitAllSuccessfulOrThrow()` — this is the fail-fast policy
3. Use `scope.join()` which waits for all tasks or throws on first failure
4. Wrap everything in a try-with-resources block
5. Handle `FailedException` to catch compliance rejections

## Run the tests

```bash
mvn test -Dtest="Exercise4Test"
```

## Hints

<details>
<summary>Hint 1 — Opening a scope (Java 25 API)</summary>

```java
try (var scope = StructuredTaskScope.open(
        Joiner.awaitAllSuccessfulOrThrow())) {

    Subtask<TypeA> a = scope.fork(() -> callServiceA());
    Subtask<TypeB> b = scope.fork(() -> callServiceB());

    scope.join();  // waits for all, throws FailedException on first failure

    // All subtasks completed successfully
    TypeA resultA = a.get();
    TypeB resultB = b.get();
}
```

Note: the old `new StructuredTaskScope.ShutdownOnFailure()` API from earlier previews is gone.
Java 25 uses `StructuredTaskScope.open()` with a `Joiner` policy.
</details>

<details>
<summary>Hint 2 — Error handling</summary>

```java
try (var scope = StructuredTaskScope.open(
        Joiner.awaitAllSuccessfulOrThrow())) {
    // ... fork tasks ...
    scope.join();
    // ... use results ...
} catch (StructuredTaskScope.FailedException e) {
    // e.getCause() is the exception from the failed subtask
    if (e.getCause() instanceof MyException) {
        // handle gracefully
    }
}
```
</details>

<details>
<summary>Hint 3 — Imports you'll need</summary>

```java
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
```

And remove the `CompletableFuture` and `ExecutionException` imports.
</details>
