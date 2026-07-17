package com.aiqaframework.test;

import com.aiqaframework.core.TestConfig;
import com.aiqaframework.web.pages.ProductSearchPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/** Verifies searching for a product shows matching results in the UI. */
public class ProductSearchTest extends BaseUiTest {

    @Test(groups = "ui")
    public void searchReturnsMatchingProducts() {
        ProductSearchPage searchPage = new ProductSearchPage(driver);

        searchPage.open(TestConfig.baseUri());
        searchPage.search("Apple");

        assertTrue(searchPage.getResultNames().stream()
                .anyMatch(name -> name.toLowerCase().contains("apple")));
    }
}
