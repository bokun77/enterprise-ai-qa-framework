package com.aiqaframework.test;

import com.aiqaframework.reporting.ExecutionReportWriter;
import com.aiqaframework.web.ScreenshotCapture;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Path;

/** Records a pass/fail/skip summary for every test, capturing a screenshot on UI test failures. */
public class ExecutionListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        ExecutionReportWriter.record(testName(result), "PASS", null, null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Path screenshot = captureScreenshotIfUiTest(result);
        Throwable failure = result.getThrowable();
        String message = failure != null ? failure.getMessage() : null;

        ExecutionReportWriter.record(testName(result), "FAIL", screenshot, message);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExecutionReportWriter.record(testName(result), "SKIP", null, null);
    }

    private Path captureScreenshotIfUiTest(ITestResult result) {
        if (result.getInstance() instanceof BaseUiTest uiTest && uiTest.driver != null) {
            return ScreenshotCapture.capture(uiTest.driver, testName(result));
        }
        return null;
    }

    private String testName(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName();
    }
}
