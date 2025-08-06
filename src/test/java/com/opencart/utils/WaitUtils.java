package com.opencart.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class WaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);

    private ThreadLocal<WebDriverWait> wait = ThreadLocal.withInitial(
            () -> new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
    );

    public WebElement waitForVisibility(By locator) {
        try {
            return wait.get().until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("❌ Element not clickable after timeout: {}", locator);
            throw e;
        }
    }

    public WebElement waitForElementToBeClickable(By locator) {
        return wait.get().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitUntilEnabled(By locator) {
        return wait.get().until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return element.isEnabled() ? element : null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    public void waitForPageLoad() {
        WebDriver driver = DriverFactory.getDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .toString().equals("complete")
            );
        } catch (TimeoutException e) {
            throw new TimeoutException("❌ Timeout waiting for document.readyState to be complete", e);
        }

        // Wait for jQuery to be idle
        try {
            wait.until(webDriver ->
                    (Boolean) ((JavascriptExecutor) webDriver)
                            .executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0")
            );
        } catch (Exception ignored) {
        }
    }


    public void waitUntilInvisibilityOfElementLocated(By locator) {
        wait.get().until(driver -> {
            try {
                List<WebElement> elements = driver.findElements(locator);
                return elements.isEmpty() || !elements.get(0).isDisplayed();
            } catch (Exception e) {
                return true; // If element is not found, consider it invisible
            }
        });
    }

    public void waitForUrlContains(String urlPart) {
        wait.get().until(ExpectedConditions.urlContains(urlPart));
    }

}