package com.aiqaframework.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductSearchPage extends BasePage {

    private static final By SEARCH_TOGGLE = By.cssSelector("button[aria-label='Open search']");
    private static final By SEARCH_INPUT = By.cssSelector("#searchQuery input");
    private static final By RESULT_NAMES = By.cssSelector("app-product .name");
    private static final By ADD_TO_BASKET_BUTTON = By.cssSelector("button[aria-label='Add to Basket']");
    private static final By CART_ITEM_COUNT = By.cssSelector("button[aria-label='Show the shopping cart'] .warn-notification");
    private static final By SNACKBAR = By.cssSelector(".mat-mdc-snack-bar-label");

    public ProductSearchPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUri) {
        driver.get(baseUri);
        dismissWelcomeBanner();
    }

    public void search(String term) {
        clickWhenReady(SEARCH_TOGGLE);
        waitForVisible(SEARCH_INPUT).sendKeys(term, Keys.ENTER);
    }

    public List<String> getResultNames() {
        try {
            waitForVisible(RESULT_NAMES);
        } catch (TimeoutException noResults) {
            return List.of();
        }
        return driver.findElements(RESULT_NAMES).stream().map(WebElement::getText).toList();
    }

    public void addFirstResultToCart() {
        waitForVisible(RESULT_NAMES);
        waitForInvisible(SNACKBAR);
        clickWhenReady(ADD_TO_BASKET_BUTTON);
    }

    public String getCartItemCount() {
        return waitForVisible(CART_ITEM_COUNT).getText();
    }
}
