package vn.agest.selenium.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger LOG = LogManager.getLogger(ConfigReader.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                LOG.error("config.properties file not found!");
                throw new RuntimeException("config.properties file not found in resources folder.");
            }

            props.load(input);
            LOG.debug("Loaded config.properties successfully.");

        } catch (Exception e) {
            LOG.error("Failed to load configuration file.", e);
            throw new RuntimeException("Failed to load configuration file.", e);
        }
    }

    public static String get(String key) {
        String cliValue = System.getProperty(key);
        if (cliValue != null) {
            LOG.debug("Config override from CLI: {} = {}", key, cliValue);
            return cliValue;
        }

        String fileValue = props.getProperty(key);
        LOG.debug("Config read: {} = {}", key, fileValue);
        return fileValue;
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid integer for key {}: {} → using default {}", key, value, defaultValue);
            return defaultValue;
        }
    }
}
