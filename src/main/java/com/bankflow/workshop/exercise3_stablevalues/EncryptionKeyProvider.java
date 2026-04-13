package com.bankflow.workshop.exercise3_stablevalues;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * TODO: Exercise 3 — Stable Values
 *
 * CURRENT STATE (before):
 * - Uses volatile + double-checked locking for lazy initialization
 * - Verbose, error-prone (easy to get wrong)
 * - JVM cannot optimize the volatile field as a constant
 *
 * YOUR TASK (after):
 * 1. Replace `volatile SecretKey encryptionKey` with `StableValue<SecretKey>`
 * 2. Replace the double-checked locking block with `stableValue.orElseSet(...)`
 * 3. The result should be ~3 lines instead of ~12
 */
public class EncryptionKeyProvider {

    // TODO: Replace with StableValue<SecretKey>
    private volatile SecretKey encryptionKey;

    /**
     * Lazily initializes and returns the encryption key.
     * Uses double-checked locking — verbose and error-prone.
     */
    public SecretKey getEncryptionKey() {
        // TODO: Replace all of this with: return stableValue.orElseSet(this::generateKey);
        if (encryptionKey == null) {
            synchronized (this) {
                if (encryptionKey == null) {
                    encryptionKey = generateKey();
                }
            }
        }
        return encryptionKey;
    }

    /**
     * Generates a new AES-256 encryption key.
     * This is expensive — we only want to do it once.
     */
    private SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES not available", e);
        }
    }
}
