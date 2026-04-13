package com.bankflow.workshop.exercise1_constructors;

import com.bankflow.workshop.domain.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exercise 1 — Flexible Constructor Bodies")
class Exercise1Test {

    @Test
    @DisplayName("Valid wire transfer should be created successfully")
    void validWireTransfer() {
        WireTransfer wt = new WireTransfer(
                "BE68539007547034", new BigDecimal("1500.00"), Currency.EUR, "Alice Dupont");

        assertNotNull(wt.getTransactionId());
        assertEquals("BE68539007547034", wt.getIban());
        assertEquals(new BigDecimal("1500.00"), wt.getAmount());
        assertEquals(Currency.EUR, wt.getCurrency());
        assertEquals("Alice Dupont", wt.getBeneficiaryName());
    }

    @Test
    @DisplayName("Invalid IBAN should throw BEFORE super() is called")
    void invalidIbanShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new WireTransfer("INVALID", new BigDecimal("100.00"), Currency.EUR, "Bob"));
        assertTrue(ex.getMessage().contains("IBAN"));
    }

    @Test
    @DisplayName("Null amount should throw BEFORE super() is called")
    void nullAmountShouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new WireTransfer("BE68539007547034", null, Currency.EUR, "Bob"));
    }

    @Test
    @DisplayName("Negative amount should throw BEFORE super() is called")
    void negativeAmountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new WireTransfer("BE68539007547034", new BigDecimal("-50.00"), Currency.EUR, "Bob"));
        assertTrue(ex.getMessage().toLowerCase().contains("amount"));
    }

    @Test
    @DisplayName("Zero amount should throw BEFORE super() is called")
    void zeroAmountShouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new WireTransfer("BE68539007547034", BigDecimal.ZERO, Currency.EUR, "Bob"));
    }

    @Test
    @DisplayName("Null currency should throw BEFORE super() is called")
    void nullCurrencyShouldThrow() {
        // This test will FAIL with the current "before" code because currency is not validated.
        // After your refactoring, it should pass!
        assertThrows(IllegalArgumentException.class, () ->
                new WireTransfer("BE68539007547034", new BigDecimal("100.00"), null, "Bob"));
    }

    @Test
    @DisplayName("Verify no static validateIban helper exists (reflection check)")
    void noStaticHelperMethod() {
        // After refactoring, the static helper should be removed
        boolean hasStaticHelper;
        try {
            WireTransfer.class.getDeclaredMethod("validateIban", String.class);
            hasStaticHelper = true;
        } catch (NoSuchMethodException e) {
            hasStaticHelper = false;
        }
        assertFalse(hasStaticHelper,
                "The static validateIban() helper should be removed — validate inline before super()!");
    }
}
