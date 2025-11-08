package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.agest.selenium.core.config.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;

public class FirefoxDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(FirefoxDriverFactory.class);

    @Override
    public WebDriver createDriver() {

        boolean isRemote = ConfigReader.getBoolean("remote", false);
        String gridUrl = ConfigReader.get("gridUrl");

        LOG.info("Initializing FirefoxDriver | remote={}, gridUrl={}", isRemote, gridUrl);

        // ==================== REMOTE MODE ====================
        if (isRemote) {
            LOG.info("Creating Remote FirefoxDriver via Selenium Grid...");

            try {
                URL url = new URL(gridUrl);
                LOG.debug("Grid URL validated successfully: {}", gridUrl);

                return new RemoteWebDriver(url, OptionsFactory.firefox());

            } catch (MalformedURLException e) {
                LOG.error("❌ Invalid Selenium Grid URL: {}", gridUrl, e);
                throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
            } catch (Exception e) {
                LOG.error("❌ Failed to create Remote FirefoxDriver at Grid: {}", gridUrl, e);
                throw new RuntimeException("Failed to create remote FirefoxDriver", e);
            }
        }

        // ==================== LOCAL MODE ====================
        LOG.info("Setting up local FirefoxDriver using WebDriverManager...");

        try {
            WebDriverManager.firefoxdriver().setup();
            LOG.debug("WebDriverManager setup completed for Firefox.");
        } catch (Exception e) {
            LOG.error("❌ WebDriverManager failed for Firefox", e);
            throw new RuntimeException("WebDriverManager setup failed for Firefox", e);
        }

        LOG.info("Creating local FirefoxDriver instance...");
        return new FirefoxDriver(OptionsFactory.firefox());
    }
}
