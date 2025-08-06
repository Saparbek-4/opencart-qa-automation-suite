package com.opencart.ui.base;

import com.opencart.ui.pages.AccountPage;
import com.opencart.ui.pages.LoginPage;
import com.opencart.ui.tests.AccountPageTest;
import com.opencart.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.opencart.ui.tests.LoginTest;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshotOnFailure(result);
            }

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

    private void takeScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                File srcFile = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                File destFile = new File("screenshots/" + result.getName() + "_" + timestamp + ".png");
                FileUtils.copyFile(srcFile, destFile);
                logger.info("📸 Screenshot saved: {}", destFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("❌ Screenshot capture failed", e);
            }
        }
    }
}