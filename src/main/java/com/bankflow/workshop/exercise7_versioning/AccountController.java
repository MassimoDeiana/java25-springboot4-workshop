package com.bankflow.workshop.exercise7_versioning;

import com.bankflow.workshop.domain.Currency;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * SOLUTION: Exercise 7 (Bonus) — Native API Versioning
 *
 * Single controller with version attributes instead of separate V1/V2 controllers.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @GetMapping(value = "/{id}", version = "1")
    public AccountV1Response getAccountV1(@PathVariable String id) {
        return new AccountV1Response(
                id,
                "BE68539007547034",
                "Alice Dupont",
                new BigDecimal("10250.75")
        );
    }

    @GetMapping(value = "/{id}", version = "2")
    public AccountV2Response getAccountV2(@PathVariable String id) {
        return new AccountV2Response(
                id,
                "BE68539007547034",
                new AccountV2Response.HolderInfo("Alice Dupont", "alice@bankflow.be"),
                new AccountV2Response.MoneyAmount(new BigDecimal("10250.75"), Currency.EUR),
                Instant.now()
        );
    }

    @GetMapping(version = "1")
    public List<AccountV1Response> listAccountsV1() {
        return List.of(
                new AccountV1Response("ACC001", "BE68539007547034", "Alice Dupont", new BigDecimal("10250.75")),
                new AccountV1Response("ACC002", "BE71096123456769", "Bob Martin", new BigDecimal("5430.20"))
        );
    }

    @GetMapping(version = "2")
    public List<AccountV2Response> listAccountsV2() {
        return List.of(
                new AccountV2Response("ACC001", "BE68539007547034",
                        new AccountV2Response.HolderInfo("Alice Dupont", "alice@bankflow.be"),
                        new AccountV2Response.MoneyAmount(new BigDecimal("10250.75"), Currency.EUR),
                        Instant.now()),
                new AccountV2Response("ACC002", "BE71096123456769",
                        new AccountV2Response.HolderInfo("Bob Martin", "bob@bankflow.be"),
                        new AccountV2Response.MoneyAmount(new BigDecimal("5430.20"), Currency.EUR),
                        Instant.now())
        );
    }
}
