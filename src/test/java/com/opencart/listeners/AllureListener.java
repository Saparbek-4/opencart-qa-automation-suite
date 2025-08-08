package com.opencart.listeners;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.ByteArrayInputStream;

import com.opencart.utils.DriverFactory;

public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            // Screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("📸 Screenshot", new ByteArrayInputStream(screenshot));

            // Page Source
            String pageSource = driver.getPageSource();
            Allure.addAttachment("📄 Page Source", "text/html", pageSource, ".html");

            // Throwable message
            if (result.getThrowable() != null) {
                Allure.addAttachment("❗ Exception", result.getThrowable().toString());
            }
        }
    }

    // Other listener methods (optional)
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
}
