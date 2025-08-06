package com.opencart.utils;

import com.opencart.ui.tests.WishListTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prop = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            logger.error("Could not load config.properties file" + e);
        }
    }

    public static Properties getProperties() {
        return prop;
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

}
