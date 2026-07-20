package com.aiqaframework.test;

import com.aiqaframework.api.ApiClient;
import com.aiqaframework.core.TestConfig;
import com.aiqaframework.web.pages.RegistrationPage;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/** Verifies registration succeeds with a unique email and fails with a clear error on a duplicate. */
public class RegistrationTest extends BaseUiTest {

    private static final String SECURITY_QUESTION = "Your eldest siblings middle name?";
    private static final String PASSWORD = "Passw0rd!";

    @Test(groups = {"ui", "regression"})
    public void uniqueEmailRegistersSuccessfully() {
        String email = "qa-ui-" + UUID.randomUUID() + "@example.com";
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.open(TestConfig.baseUri());
        registrationPage.register(email, PASSWORD, PASSWORD, SECURITY_QUESTION, "blue");
        registrationPage.waitForRedirectToLogin();

        assertFalse(driver.getCurrentUrl().contains("/register"));
    }

    @Test(groups = {"ui", "regression"})
    public void duplicateEmailShowsError() {
        String email = "qa-ui-dup-" + UUID.randomUUID() + "@example.com";
        new ApiClient(TestConfig.baseUri()).post("/api/Users",
                Map.of("email", email, "password", PASSWORD, "passwordRepeat", PASSWORD));

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.open(TestConfig.baseUri());
        registrationPage.register(email, PASSWORD, PASSWORD, SECURITY_QUESTION, "blue");

        assertEquals(registrationPage.getErrorMessage(), "Email must be unique");
    }
}
