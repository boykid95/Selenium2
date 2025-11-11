package vn.agest.selenium.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.enums.PageType;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger LOG = LogManager.getLogger(ConfigReader.class);
    private static final Properties configProps = new Properties();
    private static final Properties pageTitlesProps = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                LOG.error("config.properties file not found!");
                throw new RuntimeException("config.properties file not found in resources folder.");
            }

            configProps.load(input);
            LOG.debug("Loaded config.properties successfully.");

        } catch (Exception e) {
            LOG.error("Failed to load configuration file.", e);
            throw new RuntimeException("Failed to load configuration file.", e);
        }
    }

    private ConfigReader() {
    }

    // Note: Read value from file only (no CLI override)
    public static String get(String key) {
        String value = configProps.getProperty(key);
        LOG.debug("Config read: {} = {}", key, value);
        return value;
    }

    // Note: CLI overrides handled ONLY by SystemConfig
    public static String getOrDefault(String key, String defaultValue) {
        String cli = SystemConfig.get(key);
        if (cli != null) {
            LOG.debug("CLI override: {} = {}", key, cli);
            return cli;
        }

        String fileValue = configProps.getProperty(key);
        return fileValue != null ? fileValue : defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String cli = SystemConfig.get(key);
        if (cli != null) {
            LOG.debug("CLI override: {} = {}", key, cli);
            return Boolean.parseBoolean(cli);
        }

        String fileValue = configProps.getProperty(key);
        return fileValue == null ? defaultValue : Boolean.parseBoolean(fileValue);
    }

    public static int getInt(String key, int defaultValue) {
        String cli = SystemConfig.get(key);
        if (cli != null) {
            LOG.debug("CLI override: {} = {}", key, cli);
            try {
                return Integer.parseInt(cli);
            } catch (NumberFormatException ignored) {
            }
        }

        String fileValue = configProps.getProperty(key);
        if (fileValue != null) {
            try {
                return Integer.parseInt(fileValue);
            } catch (NumberFormatException e) {
                LOG.warn("Invalid integer for key {}: {} → using default {}", key, fileValue, defaultValue);
            }
        }

        return defaultValue;
    }

    public static String getUrlFromPageType(PageType pageType) {
        
    }

    public static String getTitleFromPageType(PageType pageType) {
    }
}
