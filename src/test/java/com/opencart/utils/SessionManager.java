package com.opencart.utils;

import com.opencart.api.clients.ApiRoutes;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static String sessionId;
    private static final CookieFilter cookieFilter = new CookieFilter();

    private static final String EMAIL = UserPoolManager.acquireUser();
    private static final String PASSWORD = ConfigReader.getProperty("testUserPassword");

    public static void initLoggedSession() {
        sessionId = createSession();
    }

    private static String createSession() {
        Response response = SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("email", SessionManager.EMAIL)
                .formParam("password", SessionManager.PASSWORD)
                .post(ApiRoutes.LOGIN);

        // Check response contains successful login indication
        if (response.asString().contains("account/login")) {
            throw new RuntimeException("❌ Login failed – still on login page.");
        }

        String sessionId = response.getCookie("OCSESSID");
        if (sessionId == null) {
            throw new RuntimeException("❌ OCSESSID not found after login.");
        }

        logger.info("✅ Logged in, OCSESSID: {}", sessionId);
        return sessionId;
    }

    public static Response startGuestSession() {
        // Step 1: Trigger home to receive OCSESSID
        SpecFactory
                .getRequestSpec(cookieFilter)
                .get(ApiRoutes.HOME)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200));

        // Step 2: Clear cart
        return SpecFactory
                .getRequestSpec(cookieFilter)
                .post(ApiRoutes.REMOVE_CART)
                .then()
                .spec(SpecFactory.jsonResponseWithStatus(200))
                .extract()
                .response();
    }

    public static CookieFilter getCookieFilter() {
        return cookieFilter;
    }

    public static boolean isAuthenticated() {
        return sessionId != null && !sessionId.trim().isEmpty();
    }

    public static void logout() {
        logger.info("🔒 Logging out...");
        SpecFactory.getRequestSpec(cookieFilter)
                .get(ApiRoutes.LOGOUT)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(302));
    }
}

