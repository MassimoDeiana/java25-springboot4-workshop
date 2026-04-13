package com.bankflow.workshop.domain;

/**
 * Simple IBAN validator for workshop purposes.
 * In real life, you'd use a proper library like iban4j.
 */
public final class IbanValidator {

    private IbanValidator() {}

    public static boolean isValid(String iban) {
        if (iban == null || iban.isBlank()) {
            return false;
        }
        // Simplified: check length and country code format
        String cleaned = iban.replaceAll("\\s", "").toUpperCase();
        return cleaned.length() >= 15
                && cleaned.length() <= 34
                && Character.isLetter(cleaned.charAt(0))
                && Character.isLetter(cleaned.charAt(1))
                && Character.isDigit(cleaned.charAt(2))
                && Character.isDigit(cleaned.charAt(3));
    }
}
