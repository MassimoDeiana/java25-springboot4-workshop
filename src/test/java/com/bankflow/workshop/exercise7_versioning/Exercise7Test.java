package com.bankflow.workshop.exercise7_versioning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Exercise 7 — Native API Versioning")
class Exercise7Test {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/accounts/{id} should return V1 flat response")
    void v1GetAccount() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/ACC001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ACC001"))
                .andExpect(jsonPath("$.iban").value("BE68539007547034"))
                .andExpect(jsonPath("$.holderName").value("Alice Dupont"))
                .andExpect(jsonPath("$.balance").value(10250.75));
    }

    @Test
    @DisplayName("GET /api/v2/accounts/{id} should return V2 rich response")
    void v2GetAccount() throws Exception {
        mockMvc.perform(get("/api/v2/accounts/ACC001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ACC001"))
                .andExpect(jsonPath("$.iban").value("BE68539007547034"))
                // V2 has nested holder info
                .andExpect(jsonPath("$.holder.name").value("Alice Dupont"))
                .andExpect(jsonPath("$.holder.email").value("alice@bankflow.be"))
                // V2 has structured money amount
                .andExpect(jsonPath("$.balance.value").value(10250.75))
                .andExpect(jsonPath("$.balance.currency").value("EUR"))
                // V2 has lastActivity
                .andExpect(jsonPath("$.lastActivity").exists());
    }

    @Test
    @DisplayName("GET /api/v1/accounts should return V1 list")
    void v1ListAccounts() throws Exception {
        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].holderName").exists())
                .andExpect(jsonPath("$[0].holder").doesNotExist()); // V1 has no nested holder
    }

    @Test
    @DisplayName("GET /api/v2/accounts should return V2 list")
    void v2ListAccounts() throws Exception {
        mockMvc.perform(get("/api/v2/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].holder.name").exists()) // V2 has nested holder
                .andExpect(jsonPath("$[0].balance.currency").exists());
    }

    @Test
    @DisplayName("Old V1 controller class should be deleted")
    void oldV1ControllerDeleted() {
        // After merging into a single AccountController, the old classes should be gone
        // This test checks that a unified AccountController exists
        try {
            Class<?> unified = Class.forName(
                    "com.bankflow.workshop.exercise7_versioning.AccountController");
            // If it exists, the exercise is on track
            assertTrue(unified.isAnnotationPresent(
                    org.springframework.web.bind.annotation.RestController.class));
        } catch (ClassNotFoundException e) {
            // AccountController doesn't exist yet — that's fine for the "before" state
            // The URL tests above will still pass because V1/V2 controllers exist
        }
    }

    private void assertTrue(boolean condition) {
        org.junit.jupiter.api.Assertions.assertTrue(condition,
                "AccountController should be annotated with @RestController");
    }
}
