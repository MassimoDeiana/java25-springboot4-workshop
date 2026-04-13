# Bonus B2 — Native API Versioning

**Spring Boot 4 — Built-in API Versioning**

## What changed?

Spring Boot 4 adds native API versioning support. No more duplicating controllers or hardcoding `/v1/`, `/v2/` in paths. You declare the version as a `@GetMapping` attribute and configure the versioning strategy centrally.

## Before

```java
// Two separate controllers with hardcoded version in the URL

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountControllerV1 {

    @GetMapping("/{id}")
    public AccountV1Response getAccount(@PathVariable String id) { /* ... */ }

    @GetMapping
    public List<AccountV1Response> listAccounts() { /* ... */ }
}

@RestController
@RequestMapping("/api/v2/accounts")
public class AccountControllerV2 {

    @GetMapping("/{id}")
    public AccountV2Response getAccount(@PathVariable String id) { /* ... */ }

    @GetMapping
    public List<AccountV2Response> listAccounts() { /* ... */ }
}
```

Problems: duplicated controller logic, version scattered across classes, no central strategy.

## After

```java
// One controller, version declared per mapping

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @GetMapping(value = "/{id}", version = "1")
    public AccountV1Response getAccountV1(@PathVariable String id) { /* ... */ }

    @GetMapping(value = "/{id}", version = "2")
    public AccountV2Response getAccountV2(@PathVariable String id) { /* ... */ }

    @GetMapping(version = "1")
    public List<AccountV1Response> listAccountsV1() { /* ... */ }

    @GetMapping(version = "2")
    public List<AccountV2Response> listAccountsV2() { /* ... */ }
}
```

Central versioning config:

```java
@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment();
        // URLs stay: /api/v1/accounts/{id} and /api/v2/accounts/{id}
    }
}
```

## Key takeaways

| Before | After |
|--------|-------|
| Separate controller per version | Single controller |
| Version hardcoded in `@RequestMapping` path | `version` attribute on `@GetMapping` |
| No central versioning strategy | `WebMvcConfigurer.configureApiVersioning()` |
| Duplicated logic across V1/V2 classes | Shared logic, different return types |
| Adding V3 = new class + copy-paste | Adding V3 = new method + `version = "3"` |
