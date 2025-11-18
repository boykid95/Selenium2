package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class EdgeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(EdgeDriverFactory.class);

    @Override
    public WebDriver createLocalDriver() {
        LOG.info("[Edge] Initializing LOCAL EdgeDriver...");

        try {
            System.setProperty("webdriver.edge.driver", "drivers/edge/msedgedriver.exe");
            LOG.info("[Edge] Using driver at {}", "drivers/edge/msedgedriver.exe");

            EdgeOptions options = OptionsFactory.edge();
            LOG.debug("[Edge] Loaded EdgeOptions");

            return new EdgeDriver(options);

        } catch (Exception e) {
            LOG.error("[Edge] Failed to initialize LOCAL EdgeDriver", e);
            throw new RuntimeException("Failed to initialize local EdgeDriver", e);
        }
    }

    @Override
    public WebDriver createRemoteDriver(URL gridUrl) {
        LOG.info("[Edge] Initializing REMOTE EdgeDriver => {}", gridUrl);

        try {
            EdgeOptions options = OptionsFactory.edge();
            LOG.debug("[Edge] Loaded EdgeOptions for REMOTE");

            return new RemoteWebDriver(gridUrl, options);

        } catch (Exception e) {
            LOG.error("[Edge] Failed to initialize REMOTE EdgeDriver", e);
            throw new RuntimeException("Failed to initialize remote EdgeDriver", e);
        }
    }
}
