package com.aiqaframework.web;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Saves a screenshot of the current browser state to disk. */
public final class ScreenshotCapture {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotCapture.class);
    private static final Path SCREENSHOT_DIR = Path.of("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP_PATTERN = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");

    private ScreenshotCapture() {
    }

    /**
     * Captures the current page as a PNG file named after the given test.
     *
     * @return the path to the saved screenshot, or {@code null} if capture failed
     */
    public static Path capture(WebDriver driver, String testName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path file = SCREENSHOT_DIR.resolve(testName + "-" + LocalDateTime.now().format(TIMESTAMP_PATTERN) + ".png");
            Files.write(file, screenshot);
            return file;
        } catch (IOException | ClassCastException e) {
            LOGGER.warn("Failed to capture screenshot for {}", testName, e);
            return null;
        }
    }
}
