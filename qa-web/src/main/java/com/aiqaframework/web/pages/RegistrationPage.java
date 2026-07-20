package com.aiqaframework.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage extends BasePage {

    private static final By EMAIL_INPUT = By.id("emailControl");
    private static final By PASSWORD_INPUT = By.id("passwordControl");
    private static final By REPEAT_PASSWORD_INPUT = By.id("repeatPasswordControl");
    private static final By SECURITY_QUESTION_SELECT = By.cssSelector("mat-select[name='securityQuestion']");
    private static final By SECURITY_ANSWER_INPUT = By.id("securityAnswerControl");
    private static final By REGISTER_BUTTON = By.id("registerButton");
    private static final By ERROR_MESSAGE = By.className("error");
    private static final By LANGUAGE_SNACKBAR = By.cssSelector(".mat-mdc-snack-bar-label");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUri) {
        driver.get(baseUri + "/#/register");
        dismissWelcomeBanner();
        waitForInvisible(LANGUAGE_SNACKBAR);
    }

    public void register(String email, String password, String repeatPassword,
                          String securityQuestionText, String securityAnswer) {
        waitForVisible(EMAIL_INPUT).sendKeys(email);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        driver.findElement(REPEAT_PASSWORD_INPUT).sendKeys(repeatPassword);
        selectSecurityQuestion(securityQuestionText);
        driver.findElement(SECURITY_ANSWER_INPUT).sendKeys(securityAnswer);
        clickWhenReady(REGISTER_BUTTON);
    }

    /** mat-select is not a native &lt;select&gt;: it must be opened, then the matching mat-option clicked. */
    private void selectSecurityQuestion(String questionText) {
        clickWhenReady(SECURITY_QUESTION_SELECT);
        By option = By.xpath("//mat-option//span[contains(normalize-space(.), '" + questionText + "')]");
        clickWhenReady(option);
    }

    /**
     * The error div is rendered by *ngIf as soon as the failed-registration callback runs, but its text
     * is interpolated in a following change-detection pass; a plain visibility wait can catch it empty.
     */
    public String getErrorMessage() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    String text = d.findElement(ERROR_MESSAGE).getText();
                    return text.isEmpty() ? null : text;
                });
    }

    public void waitForRedirectToLogin() {
        waitForUrlNotContaining("/register");
    }
}
