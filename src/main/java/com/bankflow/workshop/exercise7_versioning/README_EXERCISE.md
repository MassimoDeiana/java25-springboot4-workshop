# Exercise 7 (Bonus) — Native API Versioning

## ⏱ Estimated time: 15 minutes

## 📖 What's new?

Spring Boot 4 (via Spring Framework 7) introduces **native API versioning**.
Instead of manually hardcoding versions in URLs, you use the `version` attribute
in `@GetMapping`, `@PostMapping`, etc.

The version strategy (path, header, query param, media type) is configured **once**
centrally — not scattered across controllers.

## 🎯 Your mission

Look at `AccountControllerV1.java` and `AccountControllerV2.java` — they are two
separate controllers for the same endpoint, each with a hardcoded version in the URL.

**Refactor to use native versioning:**

1. Merge both controllers into a single `AccountController`
2. Use `version = "1"` and `version = "2"` in the `@GetMapping` annotations
3. Create `ApiVersionConfig` implementing `WebMvcConfigurer` to configure path-segment versioning
4. Delete the old V1 and V2 controllers

## ✅ Run the tests

```bash
mvn test -Dtest="Exercise7Test"
```

## 💡 Hints

<details>
<summary>Hint 1 — version attribute in @GetMapping</summary>

```java
@GetMapping(path = "/{id}", version = "1")
public AccountV1Response getAccountV1(@PathVariable String id) { ... }

@GetMapping(path = "/{id}", version = "2")
public AccountV2Response getAccountV2(@PathVariable String id) { ... }
```
</details>

<details>
<summary>Hint 2 — Versioning config</summary>

```java
@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment(1); // reads version from URL path
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/v{version}",
            HandlerTypePredicate.forAnnotation(RestController.class));
    }
}
```
</details>

<details>
<summary>Hint 3 — The URLs stay the same!</summary>

After configuration, the URLs remain:
- `GET /api/v1/accounts/{id}` → calls the `version = "1"` handler
- `GET /api/v2/accounts/{id}` → calls the `version = "2"` handler

The difference is that the version routing is now framework-managed,
not hardcoded in your `@RequestMapping`.
</details>
