package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.agest.selenium.core.config.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;

public class EdgeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(EdgeDriverFactory.class);

    @Override
    public WebDriver createDriver() {

        boolean isRemote = ConfigReader.getBoolean("remote", false);
        String gridUrl = ConfigReader.get("gridUrl");

        LOG.info("Initializing EdgeDriver | remote={}, gridUrl={}", isRemote, gridUrl);

        // ==================== REMOTE ====================
        if (isRemote) {
            LOG.info("Creating Remote EdgeDriver via Selenium Grid...");

            try {
                URL url = new URL(gridUrl);
                LOG.debug("Grid URL validated successfully: {}", gridUrl);

                return new RemoteWebDriver(url, OptionsFactory.edge());

            } catch (MalformedURLException e) {
                LOG.error("❌ Invalid Selenium Grid URL: {}", gridUrl, e);
                throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);

            } catch (Exception e) {
                LOG.error("❌ Failed to create Remote EdgeDriver at Grid: {}", gridUrl, e);
                throw new RuntimeException("Failed to create remote EdgeDriver", e);
            }
        }

        // ==================== LOCAL ====================
        LOG.info("Setting up local EdgeDriver using WebDriverManager...");

        try {
            WebDriverManager.edgedriver().setup();
            LOG.debug("WebDriverManager setup completed for Edge.");
        } catch (Exception e) {
            LOG.error("❌ WebDriverManager failed for Edge", e);
            throw new RuntimeException("WebDriverManager setup failed for Edge", e);
        }

        LOG.info("Creating local EdgeDriver instance...");
        return new EdgeDriver(OptionsFactory.edge());
    }
}
