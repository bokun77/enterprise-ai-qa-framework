package com.aiqaframework.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

/** Appends a plain-text execution record for each test to a summary file under {@code target/}. */
public final class ExecutionReportWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionReportWriter.class);
    private static final Path SUMMARY_FILE = Path.of("target", "qa-reports", "execution-summary.txt");

    static {
        resetSummaryFile();
    }

    private ExecutionReportWriter() {
    }

    public static synchronized void record(String testName, String status, Path screenshot, String failureMessage) {
        StringBuilder line = new StringBuilder()
                .append(LocalDateTime.now()).append(" | ")
                .append(status).append(" | ")
                .append(testName);

        if (screenshot != null) {
            line.append(" | screenshot=").append(screenshot);
        }
        if (failureMessage != null) {
            line.append(" | message=").append(failureMessage.replace(System.lineSeparator(), " "));
        }
        line.append(System.lineSeparator());

        try {
            Files.writeString(SUMMARY_FILE, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.warn("Failed to write execution summary for {}", testName, e);
        }
    }

    private static void resetSummaryFile() {
        try {
            Files.createDirectories(SUMMARY_FILE.getParent());
            Files.deleteIfExists(SUMMARY_FILE);
        } catch (IOException e) {
            LOGGER.warn("Failed to reset execution summary file", e);
        }
    }
}
