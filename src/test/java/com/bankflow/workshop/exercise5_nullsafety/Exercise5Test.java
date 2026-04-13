package com.bankflow.workshop.exercise5_nullsafety;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exercise 5 — JSpecify Null Safety")
class Exercise5Test {

    private final AccountLookupService lookupService = new AccountLookupService();
    private final TransferService transferService = new TransferService(lookupService);

    @Test
    @DisplayName("Valid transfer between existing accounts should succeed")
    void validTransfer() {
        var result = transferService.executeTransfer(
                "BE68539007547034", "BE71096123456769", new BigDecimal("100.00"));

        assertTrue(result.success());
        assertTrue(result.message().contains("completed"));
    }

    @Test
    @DisplayName("Transfer from non-existent account should throw AccountNotFoundException")
    void fromAccountNotFound() {
        // Before: this throws NullPointerException (bug!)
        // After: this should throw AccountNotFoundException (proper handling!)
        assertThrows(AccountNotFoundException.class, () ->
                transferService.executeTransfer(
                        "NONEXISTENT", "BE71096123456769", new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Transfer to non-existent account should throw AccountNotFoundException")
    void toAccountNotFound() {
        assertThrows(AccountNotFoundException.class, () ->
                transferService.executeTransfer(
                        "BE68539007547034", "NONEXISTENT", new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Insufficient funds should return failure result, not throw")
    void insufficientFunds() {
        var result = transferService.executeTransfer(
                "BE68539007547034", "BE71096123456769", new BigDecimal("999999.00"));

        assertFalse(result.success());
        assertTrue(result.message().contains("Insufficient"));
    }

    @Test
    @DisplayName("AccountLookupService.findByIban() should have @Nullable annotation")
    void findByIbanShouldBeNullable() throws Exception {
        var method = AccountLookupService.class.getMethod("findByIban", String.class);

        boolean hasNullable = false;
        for (var annotation : method.getAnnotatedReturnType().getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
                hasNullable = true;
                break;
            }
        }

        assertTrue(hasNullable,
                "findByIban() return type should be annotated with @Nullable!");
    }

    @Test
    @DisplayName("AccountLookupService.findByCustomerId() should have @Nullable annotation")
    void findByCustomerIdShouldBeNullable() throws Exception {
        var method = AccountLookupService.class.getMethod("findByCustomerId", String.class);

        boolean hasNullable = false;
        for (var annotation : method.getAnnotatedReturnType().getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
                hasNullable = true;
                break;
            }
        }

        assertTrue(hasNullable,
                "findByCustomerId() return type should be annotated with @Nullable!");
    }

    @Test
    @DisplayName("Package should be @NullMarked")
    void packageShouldBeNullMarked() {
        Package pkg = AccountLookupService.class.getPackage();
        boolean hasNullMarked = false;
        for (var annotation : pkg.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("NullMarked")) {
                hasNullMarked = true;
                break;
            }
        }

        assertTrue(hasNullMarked,
                "Package should have @NullMarked annotation! Create a package-info.java file.");
    }
}
