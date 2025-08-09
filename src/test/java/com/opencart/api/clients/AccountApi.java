package com.opencart.api.clients;

import com.opencart.utils.SpecFactory;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;

public class AccountApi {
    private final CookieFilter cookieFilter;

    public AccountApi(CookieFilter cookieFilter) {
        this.cookieFilter = cookieFilter;
    }

    public Response sendGETRequest(String endpoint) {
        return SpecFactory.getRequestSpec(cookieFilter)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

}
