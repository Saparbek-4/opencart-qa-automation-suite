package com.opencart.utils;


import com.opencart.api.clients.ApiRoutes;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class SpecFactory {

    public static RequestSpecification getRequestSpec(CookieFilter cookieFilter) {
        return RestAssured
                .given()
                .baseUri(ApiRoutes.BASE_URI)
                .filter(cookieFilter)
                .redirects().follow(false);
    }

    public static RequestSpecification getFormRequestSpec(CookieFilter cookieFilter) {
        return RestAssured
                .given()
                .baseUri(ApiRoutes.BASE_URI)
                .filter(cookieFilter)
                .contentType("application/x-www-form-urlencoded")
                .redirects().follow(false);
    }

    public static ResponseSpecification htmlResponseWithStatus(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(ContentType.HTML)
                .build();
    }

    public static ResponseSpecification jsonResponseWithStatus(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
