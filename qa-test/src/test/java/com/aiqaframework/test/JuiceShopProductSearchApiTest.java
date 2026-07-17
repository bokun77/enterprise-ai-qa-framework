package com.aiqaframework.test;

import com.aiqaframework.api.ApiClient;
import com.aiqaframework.core.TestConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Verifies the Juice Shop product search API returns products matching the query. */
public class JuiceShopProductSearchApiTest {

    @Test(groups = "api")
    public void searchReturnsMatchingProducts() {
        ApiClient apiClient = new ApiClient(TestConfig.baseUri());

        Response response = apiClient.get("/rest/products/search?q=apple");

        assertEquals(response.statusCode(), 200);
        assertTrue(response.jsonPath().getList("data.name", String.class)
                .stream().anyMatch(name -> name.toLowerCase().contains("apple")));
    }
}
