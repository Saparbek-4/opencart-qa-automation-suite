package com.opencart.api.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.opencart.api.stepdefinitions",
                "com.opencart.api.hooks"},
        plugin = {"pretty", "html:target/cucumber-reports"},
        monochrome = true,
        tags = "@account"
)
public class CucumberTestRunner {}