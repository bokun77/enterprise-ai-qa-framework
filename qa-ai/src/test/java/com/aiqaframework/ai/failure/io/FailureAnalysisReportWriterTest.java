package com.aiqaframework.ai.failure.io;

import com.aiqaframework.ai.failure.model.FailureAnalysis;
import com.aiqaframework.ai.failure.model.FailureCategory;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FailureAnalysisReportWriterTest {

    private final FailureAnalysisReportWriter writer = new FailureAnalysisReportWriter();

    @Test
    public void formatsEmptyListAsZeroFailuresReport() {
        String report = writer.format(List.of());

        assertTrue(report.startsWith("AI QA FAILURE ANALYSIS REPORT"));
        assertTrue(report.contains("Total failures: 0"));
        assertTrue(report.contains("Summary by category"));
    }

    @Test
    public void formatsSingleFailureWithAllFields() {
        FailureAnalysis analysis = analysis(
                "com.example.LoginTest.shouldRejectInvalidPassword",
                "2026-07-20T09:14:02.331",
                FailureCategory.AUTHENTICATION_FAILURE,
                "Message indicates a login, authentication, or token failure.",
                "message=Invalid credentials for user 'qa_test'",
                1.0);

        String report = writer.format(List.of(analysis));

        assertTrue(report.contains("Total failures: 1"));
        assertTrue(report.contains("1."));
        assertTrue(report.contains("testName=com.example.LoginTest.shouldRejectInvalidPassword"));
        assertTrue(report.contains("timestamp=2026-07-20T09:14:02.331"));
        assertTrue(report.contains("category=AUTHENTICATION_FAILURE"));
        assertTrue(report.contains("confidence=1.0"));
        assertTrue(report.contains("explanation=Message indicates a login, authentication, or token failure."));
        assertTrue(report.contains("evidence=message=Invalid credentials for user 'qa_test'"));
        assertTrue(report.contains("AUTHENTICATION_FAILURE: 1"));
    }

    @Test
    public void preservesInputOrderAcrossMultipleFailures() {
        FailureAnalysis first = analysis("com.example.A.test", "t1", FailureCategory.UI_SYNCHRONIZATION, "e", "ev", 1.0);
        FailureAnalysis second = analysis("com.example.B.test", "t2", FailureCategory.API_FAILURE, "e", "ev", 1.0);
        FailureAnalysis third = analysis("com.example.C.test", "t3", FailureCategory.UNKNOWN, "e", "ev", 0.0);

        String report = writer.format(List.of(first, second, third));

        int indexA = report.indexOf("testName=com.example.A.test");
        int indexB = report.indexOf("testName=com.example.B.test");
        int indexC = report.indexOf("testName=com.example.C.test");

        assertTrue(indexA < indexB);
        assertTrue(indexB < indexC);
        assertTrue(report.contains("Total failures: 3"));
    }

    @Test
    public void summarizesCountsPerCategory() {
        List<FailureAnalysis> analyses = List.of(
                analysis("t1", "ts", FailureCategory.API_FAILURE, "e", "ev", 1.0),
                analysis("t2", "ts", FailureCategory.API_FAILURE, "e", "ev", 1.0),
                analysis("t3", "ts", FailureCategory.UNKNOWN, "e", "ev", 0.0));

        String report = writer.format(analyses);

        assertTrue(report.contains("API_FAILURE: 2"));
        assertTrue(report.contains("UNKNOWN: 1"));
    }

    @Test
    public void reportsUnknownCategoryWithZeroConfidence() {
        FailureAnalysis analysis = analysis(
                "com.example.CheckoutTest.shouldApplyDiscount", "ts", FailureCategory.UNKNOWN,
                "No rule matched the recorded failure message.",
                "No message or screenshot recorded for this failure.", 0.0);

        String report = writer.format(List.of(analysis));

        assertTrue(report.contains("category=UNKNOWN"));
        assertTrue(report.contains("confidence=0.0"));
        assertTrue(report.contains("UNKNOWN: 1"));
    }

    @Test
    public void doesNotDeduplicateRepeatedTestNames() {
        FailureAnalysis firstRun = analysis(
                "com.example.FlakyTest.shouldRetry", "2026-07-20T09:00:00", FailureCategory.ENVIRONMENT_FAILURE, "e", "ev", 1.0);
        FailureAnalysis secondRun = analysis(
                "com.example.FlakyTest.shouldRetry", "2026-07-20T09:01:00", FailureCategory.ENVIRONMENT_FAILURE, "e", "ev", 1.0);

        String report = writer.format(List.of(firstRun, secondRun));

        assertEquals(countOccurrences(report, "testName=com.example.FlakyTest.shouldRetry"), 2);
        assertTrue(report.contains("Total failures: 2"));
        assertTrue(report.contains("ENVIRONMENT_FAILURE: 2"));
    }

    @Test
    public void formatsUtf8CharactersWithoutCorruption() {
        FailureAnalysis analysis = analysis(
                "com.example.LocaleTest.shouldDisplayName", "ts", FailureCategory.ASSERTION_FAILURE,
                "expected café — résumé 中文 but got ☃",
                "message=éèêë ñ 😀", 1.0);

        String report = writer.format(List.of(analysis));

        assertTrue(report.contains("expected café — résumé 中文 but got ☃"));
        assertTrue(report.contains("evidence=message=éèêë ñ 😀"));
    }

    @Test
    public void formatsLongExplanationWithoutTruncation() {
        String longExplanation = "This failure was caused by a chain of events: ".repeat(50);
        FailureAnalysis analysis = analysis("com.example.LongTest.shouldFail", "ts",
                FailureCategory.UNKNOWN, longExplanation, "ev", 0.0);

        String report = writer.format(List.of(analysis));

        assertTrue(report.contains("explanation=" + longExplanation));
    }

    @Test
    public void formatIsDeterministicForIdenticalInput() {
        List<FailureAnalysis> analyses = List.of(
                analysis("t1", "ts1", FailureCategory.API_FAILURE, "e1", "ev1", 1.0),
                analysis("t2", "ts2", FailureCategory.UNKNOWN, "e2", "ev2", 0.0));

        String first = writer.format(analyses);
        String second = writer.format(analyses);

        assertEquals(first, second);
    }

    @Test
    public void writeCreatesMissingParentDirectories() throws IOException {
        Path outputPath = Path.of("target", "test-reports", "nested-" + System.nanoTime(), "sub", "report.txt");
        assertTrue(Files.notExists(outputPath.getParent()));

        writer.write(List.of(analysis("t1", "ts", FailureCategory.UNKNOWN, "e", "ev", 0.0)), outputPath);

        assertTrue(Files.exists(outputPath));
        String content = Files.readString(outputPath, StandardCharsets.UTF_8);
        assertTrue(content.contains("testName=t1"));
    }

    @Test
    public void writeOverwritesExistingReport() throws IOException {
        Path outputPath = Path.of("target", "test-reports", "overwrite-" + System.nanoTime() + ".txt");
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, "stale content from a previous run");

        writer.write(List.of(analysis("t1", "ts", FailureCategory.UNKNOWN, "e", "ev", 0.0)), outputPath);

        String content = Files.readString(outputPath, StandardCharsets.UTF_8);
        assertTrue(content.contains("testName=t1"));
        assertTrue(!content.contains("stale content"));
    }

    @Test
    public void writeMatchesFormatOutputExactly() throws IOException {
        Path outputPath = Path.of("target", "test-reports", "match-" + System.nanoTime() + ".txt");
        List<FailureAnalysis> analyses = List.of(
                analysis("t1", "ts", FailureCategory.API_FAILURE, "e", "ev", 1.0));

        writer.write(analyses, outputPath);

        String content = Files.readString(outputPath, StandardCharsets.UTF_8);
        assertEquals(content, writer.format(analyses));
    }

    private int countOccurrences(String haystack, String needle) {
        int count = 0;
        int index = 0;
        while ((index = haystack.indexOf(needle, index)) != -1) {
            count++;
            index += needle.length();
        }
        return count;
    }

    private FailureAnalysis analysis(String testName, String timestamp, FailureCategory category,
            String explanation, String evidence, double confidence) {
        return new FailureAnalysis(testName, timestamp, category, explanation, evidence, confidence);
    }
}
