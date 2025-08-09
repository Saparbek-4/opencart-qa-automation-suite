package com.opencart.api.hooks;

import com.opencart.api.clients.CartApi;
import com.opencart.utils.SessionManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {
    @Before(order = 0)
    public void baseSetup() {
        RestAssured.useRelaxedHTTPSValidation();
        SessionManager.resetSession();
    }

    @Before(value = "@account or @auth", order = 1)
    public void beforeAuth() {
        SessionManager.initLoggedSession();
        new CartApi(SessionManager.getCookieFilter()).removeAllItems();
    }

    @Before(value = "@guest", order = 1)
    public void beforeGuest() {
        SessionManager.startGuestSession();
    }

    @After(value = "@account or @auth")
    public void afterAuth() {
        if (SessionManager.isAuthenticated()) SessionManager.logout();
    }


}
