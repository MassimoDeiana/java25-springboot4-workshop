package com.bankflow.workshop.exercise6_httpclient;

import com.bankflow.workshop.domain.ComplianceResult;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exercise 6 — HTTP Service Clients")
class Exercise6Test {

    private static WireMockServer wireMock;

    @BeforeAll
    static void startWireMock() {
        wireMock = new WireMockServer(wireMockConfig().dynamicPort());
        wireMock.start();
        WireMock.configureFor(wireMock.port());
    }

    @AfterAll
    static void stopWireMock() {
        wireMock.stop();
    }

    @BeforeEach
    void resetStubs() {
        wireMock.resetAll();

        // Stub: POST /api/compliance/checks
        wireMock.stubFor(post(urlEqualTo("/api/compliance/checks"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "checkId": "CHK-001",
                                    "approved": true,
                                    "reason": "All checks passed",
                                    "riskLevel": "LOW"
                                }
                                """)));

        // Stub: GET /api/compliance/checks/{id}
        wireMock.stubFor(get(urlPathMatching("/api/compliance/checks/.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "checkId": "CHK-001",
                                    "approved": true,
                                    "reason": "Previously approved",
                                    "riskLevel": "LOW"
                                }
                                """)));

        // Stub: GET /api/compliance/health
        wireMock.stubFor(get(urlEqualTo("/api/compliance/health"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                        .withBody("UP")));
    }

    // ===========================================
    // Test the OLD way (ComplianceRestClient) — should still work
    // ===========================================

    @Test
    @DisplayName("[Before] Manual RestClient should validate compliance")
    void manualClientValidate() {
        RestClient restClient = RestClient.builder()
                .baseUrl(wireMock.baseUrl())
                .build();
        var client = new ComplianceRestClient(restClient);

        ComplianceResult result = client.validate(
                new ComplianceRequest("BE68539007547034", "BE71096123456769",
                        new BigDecimal("500.00"), "EUR"));

        assertTrue(result.approved());
        assertEquals("CHK-001", result.checkId());
    }

    // ===========================================
    // Test the NEW way (@HttpExchange interface)
    // ===========================================

    @Test
    @DisplayName("[After] ComplianceServiceClient interface should exist")
    void interfaceShouldExist() {
        try {
            Class<?> clazz = Class.forName(
                    "com.bankflow.workshop.exercise6_httpclient.ComplianceServiceClient");
            assertTrue(clazz.isInterface(),
                    "ComplianceServiceClient should be an interface!");
        } catch (ClassNotFoundException e) {
            fail("Create a ComplianceServiceClient interface with @HttpExchange annotations!");
        }
    }

    @Test
    @DisplayName("[After] ComplianceServiceClient should have @HttpExchange annotation")
    void shouldHaveHttpExchangeAnnotation() {
        try {
            Class<?> clazz = Class.forName(
                    "com.bankflow.workshop.exercise6_httpclient.ComplianceServiceClient");

            boolean hasAnnotation = false;
            for (var annotation : clazz.getAnnotations()) {
                if (annotation.annotationType().getSimpleName().equals("HttpExchange")) {
                    hasAnnotation = true;
                    break;
                }
            }
            assertTrue(hasAnnotation,
                    "ComplianceServiceClient should be annotated with @HttpExchange!");
        } catch (ClassNotFoundException e) {
            fail("Create the ComplianceServiceClient interface first!");
        }
    }

    @Test
    @DisplayName("[After] Declarative client should validate compliance via WireMock")
    void declarativeClientValidate() {
        try {
            Class<?> clazz = Class.forName(
                    "com.bankflow.workshop.exercise6_httpclient.ComplianceServiceClient");

            RestClient restClient = RestClient.builder()
                    .baseUrl(wireMock.baseUrl())
                    .build();

            Object client = HttpServiceProxyFactory
                    .builderFor(RestClientAdapter.create(restClient))
                    .build()
                    .createClient(clazz);

            // Call validate via reflection
            var validateMethod = clazz.getMethod("validate", ComplianceRequest.class);
            ComplianceResult result = (ComplianceResult) validateMethod.invoke(client,
                    new ComplianceRequest("BE68539007547034", "BE71096123456769",
                            new BigDecimal("500.00"), "EUR"));

            assertNotNull(result);
            assertTrue(result.approved());
            assertEquals("CHK-001", result.checkId());

        } catch (ClassNotFoundException e) {
            fail("Create the ComplianceServiceClient interface first!");
        } catch (Exception e) {
            fail("Declarative client failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("[After] Declarative client should get result by ID via WireMock")
    void declarativeClientGetResult() {
        try {
            Class<?> clazz = Class.forName(
                    "com.bankflow.workshop.exercise6_httpclient.ComplianceServiceClient");

            RestClient restClient = RestClient.builder()
                    .baseUrl(wireMock.baseUrl())
                    .build();

            Object client = HttpServiceProxyFactory
                    .builderFor(RestClientAdapter.create(restClient))
                    .build()
                    .createClient(clazz);

            var getMethod = clazz.getMethod("getResult", String.class);
            ComplianceResult result = (ComplianceResult) getMethod.invoke(client, "CHK-001");

            assertNotNull(result);
            assertEquals("CHK-001", result.checkId());

        } catch (ClassNotFoundException e) {
            fail("Create the ComplianceServiceClient interface first!");
        } catch (Exception e) {
            fail("Declarative client getResult failed: " + e.getMessage());
        }
    }
}
