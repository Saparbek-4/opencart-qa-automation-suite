package com.opencart.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final String GRID_URL = System.getenv("JENKINS_HOME") != null ?
                                            "http://selenium-hub:4444/wd/hub" :
                                            "http://localhost:4444/wd/hub";

    public static WebDriver initDriver(String browser) {
        logger.info("🚀 Initializing WebDriver for browser: {}", browser);

//        String runMode = System.getProperty("mode", System.getenv("JENKINS_HOME") != null ? "grid" : "local");
        String runMode = "grid";
        boolean isRemote = runMode.equalsIgnoreCase("grid");

        try {
            if (browser.equalsIgnoreCase("chrome")) {
                if (isRemote) {
                    ChromeOptions options = new ChromeOptions();
                    tlDriver.set(new RemoteWebDriver(new URL(GRID_URL), options));
                } else {
                    WebDriverManager.chromedriver().setup();
                    tlDriver.set(new ChromeDriver());
                }
            } else if (browser.equalsIgnoreCase("firefox")) {
                if (isRemote) {
                    FirefoxOptions options = new FirefoxOptions();
                    tlDriver.set(new RemoteWebDriver(new URL(GRID_URL), options));
                } else {
                    WebDriverManager.firefoxdriver().setup();
                    tlDriver.set(new FirefoxDriver());
                }
            } else {
                logger.error("Unsupported browser: {}", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            logger.info("WebDriver initialized successfully.");
            tlDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            tlDriver.get().manage().window().maximize();

        } catch (MalformedURLException e) {
            logger.error("Grid URL is malformed", e);
            throw new RuntimeException(e);
        }

        return getDriver();
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
            logger.info("WebDriver quit and ThreadLocal cleared.");
        }
    }
}

