package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.AccountPage;
import com.opencart.utils.DriverFactory;
import com.opencart.utils.UserPoolManager;
import com.opencart.utils.WaitUtils;
import org.testng.annotations.BeforeMethod;
import com.opencart.ui.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @Override
    public void setupTestData() {
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();
    }

    @Test(description = "Valid login should redirect to My Account page")
    public void testValidLogin() {

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);

        AccountPage accountPage = new AccountPage();
        Assert.assertTrue(accountPage.isMyAccountHeaderVisible(), "Login failed - My Account page not visible");
    }

    @Test(description = "Invalid login should show warning")
    public void testInvalidLogin() {

        loginPage.login("invalid@example.com", "wrongpassword");

        String actualUrl = DriverFactory.getDriver().getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("login"), "Invalid login did not stay on login page");
    }
}