package com.aiqaframework.ai.failure.io;

import com.aiqaframework.ai.failure.model.FailureAnalysis;
import com.aiqaframework.ai.failure.model.FailureCategory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Formats {@link FailureAnalysis} records into a plain-text report and writes it to disk.
 *
 * Formatting and writing only: never classifies failures, never reads
 * {@code execution-summary.txt}, and consumes only {@code qa-ai}'s own output model
 * ({@link FailureAnalysis}), per ADR-0008. Output belongs to {@code qa-ai} alone and must never
 * be written into a {@code qa-reporting} directory.
 */
public final class FailureAnalysisReportWriter {

    /** {@code qa-ai}-owned output location for the failure analysis report. */
    public static final Path DEFAULT_OUTPUT_PATH = Path.of("target", "qa-ai-reports", "failure-analysis-report.txt");

    private static final String HEADER = "AI QA FAILURE ANALYSIS REPORT";
    private static final String NEWLINE = System.lineSeparator();

    /**
     * Formats {@code analyses} into a deterministic, human-readable plain-text report.
     * Input order is preserved exactly - no sorting, no filtering.
     */
    public String format(List<FailureAnalysis> analyses) {
        StringBuilder report = new StringBuilder();
        report.append(HEADER).append(NEWLINE)
                .append(NEWLINE)
                .append("Total failures: ").append(analyses.size()).append(NEWLINE);

        int index = 1;
        for (FailureAnalysis analysis : analyses) {
            report.append(NEWLINE)
                    .append(index).append(".").append(NEWLINE)
                    .append("testName=").append(analysis.testName()).append(NEWLINE)
                    .append("timestamp=").append(analysis.sourceTimestamp()).append(NEWLINE)
                    .append("category=").append(analysis.category()).append(NEWLINE)
                    .append("confidence=").append(analysis.confidence()).append(NEWLINE)
                    .append("explanation=").append(analysis.explanation()).append(NEWLINE)
                    .append("evidence=").append(analysis.evidence()).append(NEWLINE);
            index++;
        }

        report.append(NEWLINE).append("Summary by category").append(NEWLINE).append(NEWLINE);
        Map<FailureCategory, Long> counts = countByCategory(analyses);
        for (FailureCategory category : FailureCategory.values()) {
            Long count = counts.get(category);
            if (count != null) {
                report.append(category).append(": ").append(count).append(NEWLINE);
            }
        }

        return report.toString();
    }

    /**
     * Writes {@code format(analyses)} to {@code outputPath} as UTF-8, overwriting any existing
     * file and creating parent directories if missing. Propagates {@link IOException} rather
     * than swallowing it - a failed write must fail the CI step that requested this artifact,
     * not silently omit it.
     */
    public void write(List<FailureAnalysis> analyses, Path outputPath) throws IOException {
        Path parent = outputPath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(outputPath, format(analyses), StandardCharsets.UTF_8);
    }

    private Map<FailureCategory, Long> countByCategory(List<FailureAnalysis> analyses) {
        Map<FailureCategory, Long> counts = new EnumMap<>(FailureCategory.class);
        for (FailureAnalysis analysis : analyses) {
            counts.merge(analysis.category(), 1L, Long::sum);
        }
        return counts;
    }
}
