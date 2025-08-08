package com.opencart.ui.base;

import com.opencart.ui.pages.AccountPage;
import com.opencart.ui.pages.LoginPage;
import com.opencart.utils.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private AccountPage accountPage;
    private LoginPage loginPage;


    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void baseSetup(@Optional("chrome") String browserName) {
        if (System.getProperty("browser") != null) {
            browserName = System.getProperty("browser");
        }

        DriverFactory.initDriver(browserName);

        DriverFactory.getDriver().manage().deleteAllCookies();
        logger.info("🧼 Cleared cookies before test");

        setupTestData();
    }

    protected void setupTestData() {
        // For override
    }

    protected Properties getProp() {
        return ConfigReader.getProperties();
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DriverFactory.getDriver();

        try {
            if (driver != null && isUserLoggedIn()) {
                accountPage = new AccountPage();
                accountPage.clickLogout();
                logger.info("🔒 Logged out user: {}", UserPoolManager.getCurrentUser());
            }

            if (driver != null) {
                driver.manage().deleteAllCookies();
                logger.info("🧹 Deleted cookies after test");
            }

        } catch (Exception e) {
            logger.warn("⚠️ Teardown exception", e);
        } finally {
            DriverFactory.quitDriver();
            UserPoolManager.releaseUser();
            logger.info("🔚 WebDriver closed and user released");
        }
    }

    @Attachment(value = "📸 Screenshot", type = "image/png")
    public byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

    @Attachment(value = "📄 Page Source", type = "text/html")
    public String attachPageSource(String html) {
        return html;
    }

    @Attachment(value = "{title}", type = "text/plain")
    public String attachTextLog(String title, String message) {
        return message;
    }


    @Step("Check if user is logged in based on URL and Logout link")
    protected boolean isUserLoggedIn() {
        try {
            WebDriver driver = DriverFactory.getDriver();
            String currentUrl = driver.getCurrentUrl();
            logger.debug("🔍 Checking if user is logged in, URL: {}", currentUrl);

            // Method 1: Check if current URL indicates logged-in state
            if (currentUrl.contains("route=account/") && !currentUrl.contains("route=account/login")) {
                return true;
            }

            // Method 2: Check for logout element presence
            try {
                // Look for logout link without navigating anywhere
                driver.findElement(By.linkText("Logout"));
                return true;
            } catch (Exception e) {
                // Logout link not found, user likely not logged in
                return false;
            }

        } catch (Exception e) {
            logger.warn("⚠️ Exception during login check", e);
            return false;
        }
    }

}