package com.opencart.api.stepdefinitions;

import com.opencart.api.clients.AccountApi;
import com.opencart.utils.SessionManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class AccountApiStepDefinitions extends BaseApiStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(AccountApiStepDefinitions.class);
    private final AccountApi accountApi = new AccountApi(SessionManager.getCookieFilter());

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        logger.info("➡️ Sending GET request to endpoint: {}", endpoint);
        response = accountApi.sendGETRequest(endpoint);
        logger.debug("📥 Response: {}", response.asString());
    }

    @Then("the response should contain:")
    public void the_response_should_contain(DataTable dataTable) {
        List<String> expectedStrings = dataTable.asList();
        String responseBody = response.asString();
        logger.info("🔍 Verifying response contains expected values");
        for (String expected : expectedStrings) {
            logger.debug("🔹 Checking for: {}", expected);
            assertThat("❌ Missing expected string: " + expected, responseBody, containsString(expected));
        }
    }
}
