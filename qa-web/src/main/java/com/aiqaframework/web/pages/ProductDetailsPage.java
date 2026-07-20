package com.aiqaframework.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private static final By DIALOG_CONTAINER = By.cssSelector(".mat-mdc-dialog-container");
    private static final By PRODUCT_NAME = By.cssSelector(".container h1");
    private static final By PRODUCT_PRICE = By.cssSelector(".item-price");
    private static final By CLOSE_BUTTON = By.cssSelector("button[aria-label='Close Dialog']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return waitForVisible(DIALOG_CONTAINER).isDisplayed();
        } catch (TimeoutException notVisible) {
            return false;
        }
    }

    public String getProductName() {
        return waitForVisible(PRODUCT_NAME).getText();
    }

    public boolean isPriceVisible() {
        return waitForVisible(PRODUCT_PRICE).isDisplayed();
    }

    public void close() {
        clickWhenReady(CLOSE_BUTTON);
    }

    public boolean isDismissed() {
        try {
            waitForInvisible(DIALOG_CONTAINER);
            return true;
        } catch (TimeoutException stillVisible) {
            return false;
        }
    }
}
