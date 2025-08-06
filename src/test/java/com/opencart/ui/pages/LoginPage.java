package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import com.opencart.utils.ConfigReader;
import com.opencart.utils.DriverFactory;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // --- Locators ---
    private final By emailField = By.id("input-email");
    private final By passwordField = By.id("input-password");
    private final By loginButton = By.cssSelector("input[value='Login']");

    /**
     * Constructor: Initializes LoginPage and logs creation.
     */
    public LoginPage() {
        super();
        logger.info("✅ LoginPage instance created");
    }

    /**
     * Navigates to the login page of the application.
     */
    public void navigateToLoginPage() {
        String loginUrl = ConfigReader.getProperty("baseUrl") + "/index.php?route=account/login";
        DriverFactory.getDriver().navigate().to(loginUrl);
        logger.info("Navigated to: {}", DriverFactory.getDriver().getCurrentUrl());
    }

    /**
     * Logs in using provided credentials.
     *
     * @param email    User email
     * @param password User password
     */
    public void login(String email, String password) {
        logger.info("🔐 Attempting login with email: {}", email);
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
        logger.info("📩 Login submitted");
    }
}
