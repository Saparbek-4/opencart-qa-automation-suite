package com.opencart.api.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonStepDefinitions extends BaseApiStepDefinitions {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Given("I am logged in with a valid session")
    public void i_am_logged_in_with_a_valid_session() {
        logger.info("🟢 Already logged in through Hooks");
    }

    @Then("the response status code should be {int}")
    public void response_status_code_should_be(int statusCode) {
        validateStatusCode(statusCode);
    }

    @Then("the response should contain {string}")
    public void response_should_contain(String expected) {
        validateBodyContains(expected);
    }

    @Then("the response JSON should contain {string}")
    public void theResponseJSONShouldContain(String expectedMessage) {
        validateJsonFieldContains("success", expectedMessage);
    }
}
