package com.bankflow.workshop.exercise3_stablevalues;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.lang.StableValue;
import java.security.NoSuchAlgorithmException;

/**
 * SOLUTION: Exercise 3 — Stable Values (JEP 502)
 *
 * StableValue replaces volatile + double-checked locking.
 * Thread-safe by construction, JVM constant-folds after first set.
 */
public class EncryptionKeyProvider {

    private final StableValue<SecretKey> encryptionKey = StableValue.of();

    public SecretKey getEncryptionKey() {
        return encryptionKey.orElseSet(this::generateKey);
    }

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
