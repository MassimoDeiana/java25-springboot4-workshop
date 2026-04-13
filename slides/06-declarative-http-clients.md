# Bonus B1 — Declarative HTTP Service Clients

**Spring Boot 4 — @HttpExchange**

## What changed?

Spring Boot 4 promotes `@HttpExchange` interfaces as a first-class alternative to manual `RestClient` calls. You define an interface, Spring generates the implementation. Same idea as Spring Data repositories but for HTTP.

## Before

```java
public class ComplianceRestClient {

    private final RestClient restClient;

    public ComplianceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ComplianceResult validate(ComplianceRequest request) {
        return restClient.post()
                .uri("/api/compliance/checks")
                .body(request)
                .retrieve()
                .body(ComplianceResult.class);
    }

    public ComplianceResult getResult(String checkId) {
        return restClient.get()
                .uri("/api/compliance/checks/{id}", checkId)
                .retrieve()
                .body(ComplianceResult.class);
    }

    public String healthCheck() {
        return restClient.get()
                .uri("/api/compliance/health")
                .retrieve()
                .body(String.class);
    }
}
```

Every method repeats the same boilerplate: build URI, set body, retrieve, deserialize.

## After

```java
@HttpExchange("/api/compliance")
public interface ComplianceServiceClient {

    @PostExchange("/checks")
    ComplianceResult validate(@RequestBody ComplianceRequest request);

    @GetExchange("/checks/{id}")
    ComplianceResult getResult(@PathVariable String id);

    @GetExchange("/health")
    String healthCheck();
}
```

Wiring (configuration class):

```java
@Configuration
public class ComplianceClientConfig {

    @Bean
    public ComplianceServiceClient complianceServiceClient(
            @Value("${bankflow.compliance.url}") String baseUrl) {

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(ComplianceServiceClient.class);
    }
}
```

## Key takeaways

| Manual RestClient | @HttpExchange interface |
|-------------------|------------------------|
| Imperative boilerplate per method | Declarative annotations |
| URL building scattered in code | URLs centralized in interface |
| Hard to mock in tests | Interface = natural mock boundary |
| N methods = N copies of boilerplate | N methods = N one-liners |
