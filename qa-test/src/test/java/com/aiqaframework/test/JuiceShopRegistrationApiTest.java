package com.aiqaframework.test;

import com.aiqaframework.api.ApiClient;
import com.aiqaframework.core.TestConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/** Verifies the Juice Shop registration API (POST /api/Users, POST /api/SecurityAnswers). */
public class JuiceShopRegistrationApiTest {

    private static final String PASSWORD = "Passw0rd!";

    @Test(groups = {"api", "regression"})
    public void registerWithUniqueEmailReturnsCreated() {
        ApiClient apiClient = new ApiClient(TestConfig.baseUri());
        String email = "qa-api-" + UUID.randomUUID() + "@example.com";

        Response response = apiClient.post("/api/Users",
                Map.of("email", email, "password", PASSWORD, "passwordRepeat", PASSWORD));

        assertEquals(response.statusCode(), 201);
        assertEquals(response.jsonPath().getString("data.email"), email);
        assertNotNull(response.jsonPath().getString("data.id"));
    }

    @Test(groups = {"api", "regression"})
    public void registerWithDuplicateEmailReturnsValidationError() {
        ApiClient apiClient = new ApiClient(TestConfig.baseUri());
        String email = "qa-api-dup-" + UUID.randomUUID() + "@example.com";
        Map<String, Object> payload = Map.of("email", email, "password", PASSWORD, "passwordRepeat", PASSWORD);

        apiClient.post("/api/Users", payload);
        Response response = apiClient.post("/api/Users", payload);

        assertEquals(response.statusCode(), 400);
        assertEquals(response.jsonPath().getString("errors[0].field"), "email");
    }

    @Test(groups = {"api", "regression"})
    public void securityAnswerCanBeSavedForRegisteredUser() {
        ApiClient apiClient = new ApiClient(TestConfig.baseUri());
        String email = "qa-api-sq-" + UUID.randomUUID() + "@example.com";

        Response registerResponse = apiClient.post("/api/Users",
                Map.of("email", email, "password", PASSWORD, "passwordRepeat", PASSWORD));
        int userId = registerResponse.jsonPath().getInt("data.id");

        Response response = apiClient.post("/api/SecurityAnswers",
                Map.of("UserId", userId, "SecurityQuestionId", 1, "answer", "blue"));

        assertEquals(response.statusCode(), 201);
        assertEquals(response.jsonPath().getInt("data.UserId"), userId);
    }
}
