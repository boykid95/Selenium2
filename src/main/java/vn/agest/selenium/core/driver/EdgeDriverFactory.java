package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.agest.selenium.core.constants.Constants;
import vn.agest.selenium.core.log.LoggerManager;

import java.net.URL;

public class EdgeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LoggerManager.getLogger(EdgeDriverFactory.class);

    @Override
    public WebDriver createLocalDriver() {
        LOG.info("[Edge] Initializing LOCAL EdgeDriver...");

        EdgeOptions options = OptionsFactory.edge();
        LOG.debug("[Edge] Loaded EdgeOptions");

        try {
            LOG.debug("[Edge] Trying Selenium Manager (auto-detect driver)...");
            return new EdgeDriver(options);

        } catch (Exception autoEx) {
            LOG.error("[Edge] Selenium Manager failed → switching to LOCAL driver", autoEx);
        }

        try {
            System.setProperty("webdriver.edge.driver", Constants.EDGE_DRIVER_PATH);
            LOG.debug("[Edge] Using fallback LOCAL driver → {}", Constants.EDGE_DRIVER_PATH);

            return new EdgeDriver(options);

        } catch (Exception localEx) {
            LOG.error("[Edge] Failed to initialize LOCAL EdgeDriver", localEx);
            throw new RuntimeException("Cannot initialize EdgeDriver in BOTH modes", localEx);
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
