package com.opencart.ui.base;


import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencart.utils.DriverFactory;
import com.opencart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class BasePage {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);

    protected ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(DriverFactory::getDriver);
    protected ThreadLocal<WaitUtils> wait = ThreadLocal.withInitial(WaitUtils::new);

    public void click(By locator) {
        int attempts = 0;
        Exception lastException = null;

        logger.info("🖱️ Attempting click on element: {}", locator);
        while (attempts < 3) {
            try {
                WebElement element = wait.get().waitForElementToBeClickable(locator);

                // Scroll element into view
                ((JavascriptExecutor) driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);

                Thread.sleep(200);

                if (element.isEnabled() && element.isDisplayed()) {
                    element.click();
                    Thread.sleep(500);
                    logger.info("✅ Standard click successful: {}", locator);
                    return;
                }

            } catch (Exception e) {
                lastException = e;
                logger.warn("⚠️ Click attempt {} failed for locator {}: {}", attempts + 1, locator, e.getMessage());
                sleep(1000);
            }
            attempts++;
        }

        try {
            WebElement element = wait.get().waitForVisibility(locator);
            ((JavascriptExecutor) driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(200);
            ((JavascriptExecutor) driver.get()).executeScript("arguments[0].click();", element);
            Thread.sleep(500);
            logger.info("JavaScript click successful for locator: {}", locator);
        } catch (Exception jsEx) {
            logger.error("❌ All click attempts failed for locator: {}", locator, jsEx);
            throw new RuntimeException("Click failed after retries and JS fallback: " + locator, lastException);
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("🛑 Sleep interrupted", e);
        }
    }

    public void type(By locator, String text) {
        logger.info("⌨️ Typing '{}' into {}", text, locator);
        clear(locator);
        wait.get().waitForVisibility(locator).sendKeys(text);
    }

    public void clear(By locator) {
        logger.debug("🧽 Clearing text from {}", locator);
        WebElement webElement = wait.get().waitForVisibility(locator);
        webElement.clear();
    }

    public String getText(By locator) {
        String text = wait.get().waitForVisibility(locator).getText();
        logger.debug("📄 Text of {}: '{}'", locator, text);
        return text;
    }

    public String getAttribute(By locator, String attribute) {
        String value = wait.get().waitForVisibility(locator).getAttribute(attribute);
        logger.debug("🔍 Attribute '{}' of {}: '{}'", attribute, locator, value);
        return value;
    }

    public String getCurrentUrl() {
        String url = driver.get().getCurrentUrl();
        logger.debug("🌐 Current URL: {}", url);
        return url;
    }

    public boolean isElementDisplayed(By locator) {
        boolean visible = wait.get().waitForVisibility(locator).isDisplayed();
        logger.debug("👁️ Element displayed {}: {}", locator, visible);
        return visible;
    }

    public boolean isElementPresent(By locator) {
        boolean present = !driver.get().findElements(locator).isEmpty();
        logger.debug("📌 Element present {}: {}", locator, present);
        return present;
    }

}
