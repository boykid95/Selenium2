package vn.agest.selenium.core;

import org.openqa.selenium.WebDriver;
import vn.agest.selenium.enums.BrowserType;

public class DriverFactory {
    private static WebDriver driver;

    public static WebDriver getDriver(String browser) {
        if (driver == null) {
            WebDriverFactory factory = getFactory(browser);
            driver = factory.createDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }
    private static WebDriverFactory getFactory(String browser) {
        BrowserType browserType = BrowserType.fromString(browser);
        return switch (browserType) {
            case CHROME -> new ChromeDriverFactory();
            case FIREFOX -> new FirefoxDriverFactory();
            case EDGE -> new EdgeDriverFactory();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}