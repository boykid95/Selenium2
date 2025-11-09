package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class ChromeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LogManager.getLogger(ChromeDriverFactory.class);

    @Override
    public WebDriver createLocalDriver() {
        LOG.info("[Chrome] Initializing LOCAL ChromeDriver...");

        try {
            WebDriverManager.chromedriver().setup();
            LOG.debug("[Chrome] WebDriverManager setup completed.");
            return new ChromeDriver(OptionsFactory.chrome());
        } catch (Exception e) {
            LOG.error("[Chrome] Failed to initialize LOCAL ChromeDriver", e);
            throw new RuntimeException("Failed to initialize local ChromeDriver", e);
        }
    }

    @Override
    public WebDriver createRemoteDriver(URL gridUrl) {
        LOG.info("[Chrome] Initializing REMOTE ChromeDriver via Grid URL: {}", gridUrl);

        try {
            return new RemoteWebDriver(gridUrl, OptionsFactory.chrome());
        } catch (Exception e) {
            LOG.error("[Chrome] Failed to initialize REMOTE ChromeDriver", e);
            throw new RuntimeException("Failed to initialize remote ChromeDriver", e);
        }
    }
}
