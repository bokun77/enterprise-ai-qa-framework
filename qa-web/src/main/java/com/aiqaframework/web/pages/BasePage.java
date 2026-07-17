package com.aiqaframework.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final By WELCOME_BANNER_CLOSE = By.cssSelector("button[aria-label='Close Welcome Banner']");

    protected final WebDriver driver;
    private final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    protected void dismissWelcomeBanner() {
        try {
            waitForClickable(WELCOME_BANNER_CLOSE).click();
        } catch (org.openqa.selenium.TimeoutException ignored) {
        }
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForUrlNotContaining(String fragment) {
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains(fragment)));
    }
}
