package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.config.ConfigReader;
import vn.agest.selenium.enums.BrowserType;

import java.net.URL;

public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver createInstance(String browser) {

        WebDriverFactory factory = switch (BrowserType.fromString(browser)) {
            case CHROME -> new ChromeDriverFactory();
            case FIREFOX -> new FirefoxDriverFactory();
            case EDGE -> new EdgeDriverFactory();
        };

        boolean isRemote = ConfigReader.getBoolean("remote", false);

        try {
            WebDriver driver = isRemote
                    ? factory.createRemoteDriver(new URL(ConfigReader.get("gridUrl")))
                    : factory.createLocalDriver();

            DriverWindowManager.apply(driver);
            LOG.info("WebDriver initialized [{}] (remote={})", browser, isRemote);
            return driver;

        } catch (Exception e) {
            LOG.error("WebDriver creation failed for browser: {}", browser, e);
            throw new RuntimeException(e);
        }
    }
}
