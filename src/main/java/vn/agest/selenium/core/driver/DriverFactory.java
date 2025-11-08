package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.enums.BrowserType;

public class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    public static WebDriver createInstance(String browser) {
        LOG.info("Requested browser: {}", browser);

        BrowserType type;
        try {
            type = BrowserType.fromString(browser);
            LOG.debug("Mapped browser '{}' to BrowserType.{}", browser, type);
        } catch (Exception e) {
            LOG.error("Invalid browser type provided: {}", browser);
            throw e;
        }

        WebDriverFactory factory = switch (type) {
            case CHROME -> new ChromeDriverFactory();
            case FIREFOX -> new FirefoxDriverFactory();
            case EDGE -> new EdgeDriverFactory();
            default -> {
                LOG.error("Unsupported BrowserType: {}", type);
                throw new IllegalArgumentException("Unsupported browser: " + type);
            }
        };

        LOG.info("Initializing WebDriver using factory: {}", factory.getClass().getSimpleName());

        try {
            WebDriver driver = factory.createDriver();
            driver.manage().window().maximize();

            LOG.info("WebDriver initialized successfully: {}", type);
            return driver;

        } catch (Exception e) {
            LOG.error("Failed to create WebDriver for browser: {}", type, e);
            throw new RuntimeException("Failed to initialize WebDriver for: " + type, e);
        }
    }
}
