package com.opencart.ui.base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.opencart.ui.tests.LoginTest;
import com.opencart.utils.ConfigReader;
import com.opencart.utils.DriverFactory;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import com.opencart.utils.SessionManager;
import com.opencart.utils.UserPoolManager;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class BaseTest {
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void baseSetup(@Optional("chrome") String browserName) {
        // 1. Init driver
        if (System.getProperty("browser") != null) {
            browserName = System.getProperty("browser");
        }


        DriverFactory.initDriver(browserName);
        DriverFactory.getDriver().manage().deleteAllCookies();

        // 2. Acquire user
        String email = UserPoolManager.acquireUser();

        // 3. Login
        if (!this.getClass().equals(LoginTest.class)) {
            SessionManager.loginWithSessionCookie(
                    DriverFactory.getDriver(),
                    getProp().getProperty("baseUrl"),
                    email,
                    getProp().getProperty("testUserPassword")
            );
        }

        setupTestData();
    }

    protected void setupTestData() {}

    protected Properties getProp() {
        return ConfigReader.getProperties();
    }

    protected void navigateToHomePage() {
        DriverFactory.getDriver().get(getProp().getProperty("baseUrl"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        DriverFactory.quitDriver();
        UserPoolManager.releaseUser();
    }

    private void takeScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                File srcFile = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                File destFile = new File("screenshots/" + result.getName() + "_" + timestamp + ".png");
                FileUtils.copyFile(srcFile, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}