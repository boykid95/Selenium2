package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.log.LoggerManager;

public final class DriverManager {

    private static final Logger LOG = LoggerManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver(String browser) {
        if (TL_DRIVER.get() == null) {
            LOG.info("Initializing WebDriver for browser: {}", browser);

            try {
                WebDriver driver = DriverFactory.createInstance(browser);
                TL_DRIVER.set(driver);
                LOG.info("WebDriver initialized successfully for [{}]", browser);
            } catch (Exception e) {
                LOG.error("Failed to initialize WebDriver for browser: {}", browser, e);
                throw new RuntimeException("Unable to start WebDriver for browser: " + browser, e);
            }
        } else {
            LOG.warn("WebDriver already initialized for this thread. Skipping init.");
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = TL_DRIVER.get();
        if (driver == null) {
            LOG.error("WebDriver is NOT initialized before use!");
            throw new IllegalStateException(
                    "WebDriver is not initialized. Call DriverManager.initDriver() FIRST."
            );
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = TL_DRIVER.get();

        if (driver != null) {
            LOG.info("Quitting WebDriver...");
            try {
                driver.quit();
                LOG.info("WebDriver closed successfully.");
            } catch (Exception e) {
                LOG.error("Error while quitting WebDriver", e);
            } finally {
                TL_DRIVER.remove();
                LOG.debug("ThreadLocal WebDriver removed.");
            }
        } else {
            LOG.warn("quitDriver() called but WebDriver was NULL.");
        }
    }
}
