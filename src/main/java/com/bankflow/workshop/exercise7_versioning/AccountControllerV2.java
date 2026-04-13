package com.bankflow.workshop.exercise7_versioning;

import com.bankflow.workshop.domain.Currency;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * TODO: Exercise 7 — Native API Versioning
 *
 * CURRENT STATE (before):
 * - Second controller just for V2 — lots of duplication
 * - Version baked into the URL path
 *
 * YOUR TASK:
 * - Merge into the unified AccountController
 * - Delete this class
 */
@RestController
@RequestMapping("/api/v2/accounts")
public class AccountControllerV2 {

    @GetMapping("/{id}")
    public AccountV2Response getAccount(@PathVariable String id) {
        // Simulated data lookup — richer V2 response
        return new AccountV2Response(
                id,
                "BE68539007547034",
                new AccountV2Response.HolderInfo("Alice Dupont", "alice@bankflow.be"),
                new AccountV2Response.MoneyAmount(new BigDecimal("10250.75"), Currency.EUR),
                Instant.now()
        );
    }

    @GetMapping
    public java.util.List<AccountV2Response> listAccounts() {
        return java.util.List.of(
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
