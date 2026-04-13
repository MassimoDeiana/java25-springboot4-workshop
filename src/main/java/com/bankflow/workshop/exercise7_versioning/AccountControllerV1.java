package com.bankflow.workshop.exercise7_versioning;

import com.bankflow.workshop.domain.Currency;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * TODO: Exercise 7 — Native API Versioning
 *
 * CURRENT STATE (before):
 * - Separate controller per version
 * - Version hardcoded in @RequestMapping URL
 * - Duplicated class structure
 * - If you change versioning strategy (path → header), you rewrite ALL controllers
 *
 * YOUR TASK:
 * - Merge this into a single AccountController with version = "1" / "2" attributes
 * - Delete this class and AccountControllerV2
 */
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountControllerV1 {

    @GetMapping("/{id}")
    public AccountV1Response getAccount(@PathVariable String id) {
        // Simulated data lookup
        return new AccountV1Response(
                id,
                "BE68539007547034",
                "Alice Dupont",
                new BigDecimal("10250.75")
        );
    }

    @GetMapping
    public java.util.List<AccountV1Response> listAccounts() {
        return java.util.List.of(
                new AccountV1Response("ACC001", "BE68539007547034", "Alice Dupont", new BigDecimal("10250.75")),
                new AccountV1Response("ACC002", "BE71096123456769", "Bob Martin", new BigDecimal("5430.20"))
        );
    }
}
