package com.opencart.api.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        tags = "@smoke",
        features = "src/test/resources/features",
        glue = {"com.opencart.api.stepdefinitions", "com.opencart.api.hooks"},
        plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
}
