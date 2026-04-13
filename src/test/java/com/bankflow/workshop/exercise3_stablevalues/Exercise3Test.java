package com.bankflow.workshop.exercise3_stablevalues;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exercise 3 — Stable Values")
class Exercise3Test {

    @Test
    @DisplayName("EncryptionKeyProvider should return the same key on multiple calls")
    void sameKeyOnMultipleCalls() {
        var provider = new EncryptionKeyProvider();

        SecretKey key1 = provider.getEncryptionKey();
        SecretKey key2 = provider.getEncryptionKey();
        SecretKey key3 = provider.getEncryptionKey();

        assertNotNull(key1);
        assertSame(key1, key2, "Should return the exact same instance");
        assertSame(key2, key3, "Should return the exact same instance");
    }

    @Test
    @DisplayName("EncryptionKeyProvider should return a valid AES key")
    void validAesKey() {
        var provider = new EncryptionKeyProvider();
        SecretKey key = provider.getEncryptionKey();

        assertEquals("AES", key.getAlgorithm());
        assertEquals(32, key.getEncoded().length, "AES-256 key should be 32 bytes");
    }

    @Test
    @DisplayName("EncryptionKeyProvider should NOT use volatile fields")
    void noVolatileFields() {
        for (Field field : EncryptionKeyProvider.class.getDeclaredFields()) {
            assertFalse(Modifier.isVolatile(field.getModifiers()),
                    "Field '%s' should not be volatile — use StableValue instead!".formatted(field.getName()));
        }
    }

    @Test
    @DisplayName("EncryptionKeyProvider should NOT have synchronized blocks")
    void noSynchronizedMethods() {
        for (var method : EncryptionKeyProvider.class.getDeclaredMethods()) {
            assertFalse(Modifier.isSynchronized(method.getModifiers()),
                    "Method '%s' should not be synchronized — StableValue handles thread safety!"
                            .formatted(method.getName()));
        }
    }

    @Test
    @DisplayName("RegulationConfigLoader should return consistent config")
    void regulationConfigConsistent() {
        var loader = new RegulationConfigLoader();

        var config1 = loader.getConfig();
        var config2 = loader.getConfig();

        assertNotNull(config1);
        assertSame(config1, config2, "Should return the exact same instance");
        assertEquals("EU", config1.region());
        assertTrue(config1.sanctionCheckRequired());
        assertEquals(50_000.00, config1.maxTransactionAmount());
    }

    @Test
    @DisplayName("RegulationConfigLoader should NOT use volatile fields")
    void regulationNoVolatile() {
        for (Field field : RegulationConfigLoader.class.getDeclaredFields()) {
            assertFalse(Modifier.isVolatile(field.getModifiers()),
                    "Field '%s' should not be volatile — use StableValue instead!".formatted(field.getName()));
        }
    }

    @Test
    @DisplayName("EncryptionKeyProvider should be thread-safe")
    void threadSafety() throws InterruptedException {
        var provider = new EncryptionKeyProvider();
        SecretKey[] keys = new SecretKey[10];
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            final int idx = i;
            threads[i] = Thread.ofVirtual().start(() -> {
                keys[idx] = provider.getEncryptionKey();
            });
        }
        for (Thread t : threads) t.join();

        // All threads should have gotten the same key
        for (int i = 1; i < 10; i++) {
            assertSame(keys[0], keys[i],
                    "All threads should get the same key instance");
        }
    }
}
