# Exercise 6 (Bonus) — Declarative HTTP Service Clients (@HttpExchange)

## ⏱ Estimated time: 15 minutes

## 📖 What's new?

Spring Boot 4 provides **first-class support for HTTP Service Clients**. You define a
Java interface with `@HttpExchange` annotations, and Spring generates the implementation
automatically. Think Feign, but built into Spring — no extra dependency!

Key annotations:
- `@HttpExchange("/base-path")` — on the interface
- `@GetExchange`, `@PostExchange`, `@PutExchange`, `@DeleteExchange` — on methods
- Works with `RestClient` (blocking) or `WebClient` (reactive)

## 🎯 Your mission

Look at `ComplianceRestClient.java` — it manually calls the compliance service using
`RestClient` with explicit URL building, error handling, and response parsing.

**Refactor to use `@HttpExchange`:**

1. Create a `ComplianceServiceClient` interface with `@HttpExchange` annotations
2. Create a configuration class that wires the interface to a `RestClient`
3. Replace the manual `RestClient` calls in `ComplianceRestClient` with the new interface
4. The tests use WireMock to simulate the compliance service

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise6Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — Interface definition</summary>

```java
@HttpExchange("/api/compliance")
public interface ComplianceServiceClient {

    @PostExchange("/checks")
    ComplianceResult validate(@RequestBody ComplianceRequest request);

    @GetExchange("/checks/{id}")
    ComplianceResult getResult(@PathVariable String id);
}
```
</details>

<details>
<summary>Hint 2 — Wiring with RestClient</summary>

```java
@Bean
public ComplianceServiceClient complianceServiceClient(RestClient.Builder builder) {
    RestClient restClient = builder
        .baseUrl("http://compliance-service:8081")
        .build();

    return HttpServiceProxyFactory
        .builderFor(RestClientAdapter.create(restClient))
        .build()
        .createClient(ComplianceServiceClient.class);
}
```
</details>

<details>
<summary>Hint 3 — Import for RestClientAdapter</summary>

```java
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
```
</details>
