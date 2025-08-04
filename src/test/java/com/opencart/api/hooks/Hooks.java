package com.opencart.api.hooks;

import com.opencart.api.clients.CartApi;
import com.opencart.utils.SessionManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before("@account or @auth")
    public void beforeScenario() {
        logger.info("🔐 Initializing authenticated session for test scenario...");
        RestAssured.useRelaxedHTTPSValidation();
        SessionManager.initLoggedSession(); // logs in and stores session
        new CartApi(SessionManager.getCookieFilter()).removeAllItems();
    }


    @After("@account or @auth")
    public void afterScenario() {
        logger.info("🔚 Ending test scenario, clearing session...");
        SessionManager.logout();
    }
}
