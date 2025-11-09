package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class EdgeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(EdgeDriverFactory.class);

    @Override
    public WebDriver createLocalDriver() {
        LOG.info("[Edge] Initializing LOCAL EdgeDriver...");

        try {
            WebDriverManager.edgedriver().setup();
            LOG.debug("[Edge] WebDriverManager setup completed.");
            return new EdgeDriver(OptionsFactory.edge());
        } catch (Exception e) {
            LOG.error("[Edge] Failed to initialize LOCAL EdgeDriver", e);
            throw new RuntimeException("Failed to initialize local EdgeDriver", e);
        }
    }

    @Override
    public WebDriver createRemoteDriver(URL gridUrl) {
        LOG.info("[Edge] Initializing REMOTE EdgeDriver via Grid URL: {}", gridUrl);

        try {
            return new RemoteWebDriver(gridUrl, OptionsFactory.edge());
        } catch (Exception e) {
            LOG.error("[Edge] Failed to initialize REMOTE EdgeDriver", e);
            throw new RuntimeException("Failed to initialize remote EdgeDriver", e);
        }
    }
}
