package com.aiqaframework.test;

import com.aiqaframework.api.ApiClient;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Verifies the Juice Shop products API responds successfully with product data. */
public class JuiceShopProductsApiTest {

    private static final String BASE_URI = "http://localhost:3000";

    @Test
    public void getProductsReturnsOk() {
        ApiClient apiClient = new ApiClient(BASE_URI);

        Response response = apiClient.get("/api/Products");

        assertEquals(response.statusCode(), 200);
        assertTrue(response.jsonPath().getList("data").size() > 0);
    }
}
