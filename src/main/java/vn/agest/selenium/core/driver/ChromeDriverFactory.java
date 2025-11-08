package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.agest.selenium.core.config.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;

public class ChromeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(ChromeDriverFactory.class);

    @Override
    public WebDriver createDriver() {

        boolean isRemote = ConfigReader.getBoolean("remote", false);
        String gridUrl = ConfigReader.get("gridUrl");

        LOG.info("Initializing ChromeDriver | remote={}, gridUrl={}", isRemote, gridUrl);

        // ==================== REMOTE WEB DRIVER ====================
        if (isRemote) {
            LOG.info("Creating Remote ChromeDriver via Selenium Grid...");
            try {
                URL url = new URL(gridUrl);
                LOG.debug("Grid URL validated: {}", gridUrl);

                return new RemoteWebDriver(url, OptionsFactory.chrome());

            } catch (MalformedURLException e) {
                LOG.error("❌ Invalid Selenium Grid URL: {}", gridUrl, e);
                throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
            } catch (Exception e) {
                LOG.error("❌ Failed to connect to Selenium Grid: {}", gridUrl, e);
                throw new RuntimeException("Failed to create remote ChromeDriver", e);
            }
        }

        // ==================== LOCAL WEB DRIVER ====================
        LOG.info("Setting up local ChromeDriver using WebDriverManager...");
        try {
            WebDriverManager.chromedriver().setup();
            LOG.debug("WebDriverManager setup completed for Chrome.");
        } catch (Exception e) {
            LOG.error("❌ WebDriverManager failed for Chrome", e);
            throw new RuntimeException("WebDriverManager setup failed for Chrome", e);
        }

        LOG.info("Creating local ChromeDriver instance...");
        return new ChromeDriver(OptionsFactory.chrome());
    }
}
