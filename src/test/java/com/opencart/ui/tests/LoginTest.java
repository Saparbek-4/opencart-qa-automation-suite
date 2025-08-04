package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.AccountPage;
import com.opencart.utils.DriverFactory;
import com.opencart.utils.UserPoolManager;
import org.testng.annotations.BeforeMethod;
import com.opencart.ui.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage();
    }


    @Test(description = "Valid login should redirect to My Account page")
    public void testValidLogin() {
        loginPage.navigateToLoginPage();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");


        loginPage.login(email, password);

        AccountPage accountPage = new AccountPage();
        Assert.assertTrue(accountPage.isMyAccountHeaderVisible(), "Login failed - My Account page not visible");
    }

    @Test(description = "Invalid login should show warning")
    public void testInvalidLogin() {
        loginPage.navigateToLoginPage();

        loginPage.login("invalid@example.com", "wrongpassword");

        String actualUrl = DriverFactory.getDriver().getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("login"), "Invalid login did not stay on login page");
    }
}