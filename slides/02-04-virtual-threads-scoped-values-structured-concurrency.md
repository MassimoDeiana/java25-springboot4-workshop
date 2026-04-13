# Exercises 2 & 4 — Virtual Threads, Scoped Values & Structured Concurrency

## See the interactive visual presentation

These three features are designed as **one coherent model** and are best understood together:

1. **Virtual Threads** — make threads cheap (millions instead of hundreds)
2. **Scoped Values** — share context across those cheap threads (replaces ThreadLocal)
3. **Structured Concurrency** — organize those cheap threads (parent-child, auto-cancel)

Open the visual presentation for the full walkthrough with animations and before/after comparisons:

```
open /path/to/vthread/index.html
```

Then come back here for the hands-on exercises:
- **Exercise 2** — Refactor `ThreadLocal` to `ScopedValue` in a banking audit context
- **Exercise 4** — Refactor `CompletableFuture` to `StructuredTaskScope` in a payment processor
