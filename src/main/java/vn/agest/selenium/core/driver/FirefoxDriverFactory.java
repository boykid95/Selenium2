package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class FirefoxDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(FirefoxDriverFactory.class);

    @Override
    public WebDriver createLocalDriver() {
        LOG.info("[Firefox] Initializing LOCAL FirefoxDriver...");

        try {
            WebDriverManager.firefoxdriver().setup();
            LOG.debug("[Firefox] WebDriverManager setup completed.");

            FirefoxOptions options = OptionsFactory.firefox();
            LOG.debug("[Firefox] Loaded FirefoxOptions");

            return new FirefoxDriver(options);

        } catch (Exception e) {
            LOG.error("[Firefox] Failed to initialize LOCAL FirefoxDriver", e);
            throw new RuntimeException("Failed to initialize local FirefoxDriver", e);
        }
    }

    @Override
    public WebDriver createRemoteDriver(URL gridUrl) {
        LOG.info("[Firefox] Initializing REMOTE FirefoxDriver => {}", gridUrl);

        try {
            FirefoxOptions options = OptionsFactory.firefox();
            LOG.debug("[Firefox] Loaded FirefoxOptions for REMOTE");

            return new RemoteWebDriver(gridUrl, options);

        } catch (Exception e) {
            LOG.error("[Firefox] Failed to initialize REMOTE FirefoxDriver", e);
            throw new RuntimeException("Failed to initialize remote FirefoxDriver", e);
        }
    }
}
