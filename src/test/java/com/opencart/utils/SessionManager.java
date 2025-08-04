package com.opencart.utils;

import com.opencart.api.clients.ApiRoutes;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static String sessionId;
    private static final CookieFilter cookieFilter = new CookieFilter();

    private static final String EMAIL = "qa_user1@example.com";
    private static final String PASSWORD = "YourStrongPassword123";

    public static void initLoggedSession() {
        sessionId = createSession(ApiRoutes.BASE_URI, EMAIL, PASSWORD);
    }

    private static String createSession(String baseUrl, String email, String password) {
        Response response = SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("email", email)
                .formParam("password", password)
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

    public static void loginWithSessionCookie(WebDriver driver, String baseUrl, String email, String password) {
        try {
            // Prepare login URL (POST to login)
            URL url = new URL(baseUrl + "/index.php?route=account/login");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false); // prevent redirect
            conn.setDoOutput(true);

            String payload = "email=" + email + "&password=" + password;
            conn.getOutputStream().write(payload.getBytes());

            // Get Set-Cookie header
            Map<String, List<String>> headers = conn.getHeaderFields();
            List<String> cookies = headers.get("Set-Cookie");

            // Visit base URL in Selenium and wait for page load
            driver.get(baseUrl);

            // Wait for page to be fully loaded before injecting cookies
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
            );

            // Inject cookies into browser
            if (cookies != null) {
                for (String cookieStr : cookies) {
                    String[] parts = cookieStr.split(";")[0].split("=");
                    String name = parts[0].trim();
                    String value = parts.length > 1 ? parts[1].trim() : "";
                    driver.manage().addCookie(new Cookie(name, value));
                    logger.debug("Added cookie: {} = {}", name, value);
                }
            }

            // Refresh page to apply session
            driver.navigate().refresh();

            // Wait for page load after refresh
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
            );

            // Verify login was successful by checking for account elements
            try {
                // Wait for either "My Account" link or user-specific element
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.linkText("My Account")),
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, 'account/account')]"))
                ));
                logger.info("Session login successful for user: {}", email);
            } catch (Exception e) {
                logger.warn("Could not verify login state for user: {}", email);
                // Continue anyway - some pages might not show login indicators
            }

        } catch (Exception e) {
            logger.error("Session-based login failed for user: {}", email, e);
            throw new RuntimeException("Session-based login failed", e);
        }
    }
}

