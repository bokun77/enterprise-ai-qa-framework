package com.aiqaframework.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        // Juice Shop hides the desktop nav (e.g. #navbarAccount) below Angular Material's
        // ~960px breakpoint, so a fixed desktop-sized viewport is needed for reliable
        // element visibility, especially under headless Chrome's narrow default viewport.
        options.addArguments("--window-size=1600,1000");

        if (Boolean.parseBoolean(System.getenv("CI"))) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        return new ChromeDriver(options);
    }
}
