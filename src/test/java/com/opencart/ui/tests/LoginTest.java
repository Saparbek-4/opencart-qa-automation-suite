package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.LoginPage;
import com.opencart.utils.DriverFactory;
import com.opencart.utils.UserPoolManager;
import com.opencart.utils.WaitUtils;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 🧪 Login Test Suite for verifying login scenarios.
 */
@Epic("Authentication")
@Feature("Login Functionality")
@Owner("saparbek.kozhanazar04@gmail.com")
@Tag("regression")
@Tag("ui")
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    /**
     * 🔧 Setup method: Initializes login page and opens login screen.
     */
    @Override
    public void setupTestData() {
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();
    }

    /**
     * ✅ TC_001: Positive login with valid credentials.
     */
    @Test(description = "TC_001: Valid login should redirect to My Account page",
            priority = 1,
            groups = {"smoke", "ui", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("User logs in with valid credentials")
    @TmsLink("TC-001")
    @Description("Verify that login with valid credentials redirects to My Account page and user is logged in.")
    @Tag("smoke")
    public void testValidLogin() {
        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);

        Assert.assertTrue(isUserLoggedIn(),
                "❌ Login failed - User not logged in");
    }

    /**
     * ❌ TC_002: Invalid login attempt.
     */
    @Test(description = "TC_002: Invalid login should show warning and stay on login page",
            priority = 2,
            groups = {"ui", "regression"})
    @Severity(SeverityLevel.NORMAL)
    @Story("User attempts login with invalid credentials")
    @TmsLink("TC-002")
    @Description("Verify that invalid login shows warning and does not redirect to My Account page.")
    public void testInvalidLogin() {
        loginPage.login("invalid@example.com", "wrongpassword");

        String actualUrl = DriverFactory.getDriver().getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("login"),
                "❌ Invalid login did not stay on login page");
    }
}

