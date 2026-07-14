package com.aiqaframework.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/** Minimal reusable REST client wrapping Rest Assured for API test scenarios. */
public class ApiClient {

    private final String baseUri;

    public ApiClient(String baseUri) {
        this.baseUri = baseUri;
    }

    private RequestSpecification request() {
        return RestAssured.given().baseUri(baseUri);
    }

    public Response get(String path) {
        return request().get(path);
    }

    public Response post(String path, Object body) {
        return request().contentType("application/json").body(body).post(path);
    }

    public Response put(String path, Object body) {
        return request().contentType("application/json").body(body).put(path);
    }

    public Response delete(String path) {
        return request().delete(path);
    }
}
