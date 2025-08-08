package com.opencart.api.stepdefinitions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public abstract class BaseApiStepDefinitions {

    protected static Response response;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Step("Validate response status code is {expected}")
    public void validateStatusCode(int expected) {
        if (response == null) {
            throw new IllegalStateException("❌ No response available to validate status code.");
        }

        logger.info("🔍 Validating status code. Expected: {}, Actual: {}", expected, response.statusCode());
        assertThat("❌ Status code mismatch:", response.statusCode(), equalTo(expected));
    }

    @Step("Validate response body contains: {expectedText}")
    public void validateBodyContains(String expectedText) {
        if (response == null) {
            throw new IllegalStateException("❌ No response available to validate body.");
        }
        assertThat("❌ Response does not contain expected text", response.asString(), containsString(expectedText));
    }

    @Step("Validate JSON field at path '{jsonPath}' contains: {expectedValue}")
    public void validateJsonFieldContains(String jsonPath, String expectedValue) {
        if (response == null) {
            throw new IllegalStateException("❌ No response available to validate Json Field.");
        }
        String actual = response.jsonPath().getString(jsonPath);
        assertThat("❌ JSON field mismatch", actual, containsString(expectedValue));
    }
}
