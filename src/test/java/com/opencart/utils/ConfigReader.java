package com.opencart.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prop = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            System.out.println("Could not load config.properties file");
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return prop;
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

}
