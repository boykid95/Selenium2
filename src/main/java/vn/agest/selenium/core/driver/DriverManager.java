package vn.agest.selenium.core.driver;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private DriverManager() {
    }

    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    public static void initDriver(String browser) {
        if (TL_DRIVER.get() == null) {
            TL_DRIVER.set(DriverFactory.createInstance(browser));
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = TL_DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized. Call DriverManager.initDriver() before using getDriver()."
            );
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = TL_DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                TL_DRIVER.remove();
            }
        }
    }
}
