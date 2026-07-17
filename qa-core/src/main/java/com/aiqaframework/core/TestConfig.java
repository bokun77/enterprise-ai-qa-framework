package com.aiqaframework.core;

public final class TestConfig {

    private static final String DEFAULT_BASE_URI = "http://localhost:3000";

    private TestConfig() {
    }

    public static String baseUri() {
        return System.getProperty("baseUri", DEFAULT_BASE_URI);
    }
}
