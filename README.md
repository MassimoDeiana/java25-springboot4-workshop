# ☕🍃 Java 25 & Spring Boot 4 — Hands-On Workshop

> **A banking-themed workshop where you refactor real code using the latest Java 25 and Spring Boot 4 features.**

## 🏦 The Scenario

You're working on **BankFlow**, a payment processing microservice at a bank. The codebase works, but it uses older patterns. Your mission: **modernize it** using Java 25 and Spring Boot 4 features.

## 📋 Prerequisites

- **JDK 25** (download from [Adoptium](https://adoptium.net/) or [Oracle](https://jdk.java.net/25/))
- **IDE**: IntelliJ IDEA 2025.2+ (recommended) or VS Code with Java Extension Pack
- **Maven 3.9+**
- Enable preview features in your IDE:
  - IntelliJ: `Settings → Build → Compiler → Java Compiler → Additional CLI params: --enable-preview`

## 🚀 Getting Started

```bash
# Clone and build
cd java25-springboot4-workshop
mvn clean compile

# Run the application
mvn spring-boot:run

# Run tests
mvn test
```

The app starts on `http://localhost:8080`. You can test with:

```bash
# Get an account
curl http://localhost:8080/api/v1/accounts/ACC001

# Process a payment
curl -X POST http://localhost:8080/api/v1/payments \
  -H "Content-Type: application/json" \
  -d '{"fromIban":"BE68539007547034","toIban":"BE71096123456769","amount":150.00,"currency":"EUR"}'

# Check compliance
curl http://localhost:8080/api/v1/compliance/check/PAY001
```

## 🎯 Workshop Exercises

Each exercise lives in a specific package and has a `README_EXERCISE.md` with detailed instructions.

| # | Feature | Package | Difficulty |
|---|---------|---------|------------|
| 1 | Flexible Constructor Bodies | `exercise1_constructors` | ⭐ Easy |
| 2 | Scoped Values | `exercise2_scopedvalues` | ⭐⭐ Medium |
| 3 | Stable Values | `exercise3_stablevalues` | ⭐ Easy |
| 4 | Structured Concurrency | `exercise4_concurrency` | ⭐⭐⭐ Hard |
| 5 | JSpecify Null Safety | `exercise5_nullsafety` | ⭐ Easy |

### Bonus Exercises

If you finish early or want to explore further:

| # | Feature | Package | Difficulty |
|---|---------|---------|------------|
| 6 | Declarative HTTP Service Clients | `exercise6_httpclient` | ⭐⭐ Medium |
| 7 | Native API Versioning | `exercise7_versioning` | ⭐⭐ Medium |

### How it works

1. **Read** the exercise README in each package
2. **Look at** the existing "before" code — it works, but uses old patterns
3. **Refactor** the code using the new feature described in the README
4. **Run the tests** to verify your solution: `mvn test -pl . -Dtest=Exercise*Test`

### 💡 Tips

- Each exercise is **independent** — you can do them in any order
- The `solution` branch contains the completed code
- Preview features require `--enable-preview` (already configured in `pom.xml`)
- If stuck, check the `hints/` folder in each exercise package

## 📦 Project Structure

```
src/main/java/com/bankflow/workshop/
├── BankFlowApplication.java
├── domain/                          # Shared domain model
│   ├── Account.java
│   ├── Payment.java
│   ├── ComplianceResult.java
│   └── ...
├── exercise1_constructors/          # Flexible Constructor Bodies
├── exercise2_scopedvalues/          # Scoped Values (replace ThreadLocal)
├── exercise3_stablevalues/          # Stable Values (lazy init)
├── exercise4_concurrency/           # Structured Concurrency
├── exercise5_nullsafety/            # JSpecify Null Safety
├── exercise6_httpclient/            # [Bonus] HTTP Service Clients (@HttpExchange)
└── exercise7_versioning/            # [Bonus] Native API Versioning
```

## ⏱ Estimated Timing

| Phase | Duration |
|-------|----------|
| Intro & feature overview | 15 min |
| Exercises 1-3 (Java 25 basics) | 20 min |
| Exercise 4 (Structured Concurrency) | 10 min |
| Exercise 5 (Null Safety) | 5 min |
| Wrap-up & discussion | 10 min |
| **Total** | **~1h** |

> Bonus exercises (B1, B2) are available for those who finish early or want to continue after the workshop.

---

*Built for the Java Guild — Happy coding! 🚀*
