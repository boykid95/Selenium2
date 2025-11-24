package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.agest.selenium.core.log.LoggerManager;

import java.net.URL;

public class ChromeDriverFactory implements WebDriverFactory {

    private static final Logger LOG = LoggerManager.getLogger(ChromeDriverFactory.class);

    @Override
    public WebDriver createLocalDriver() {
        LOG.warn("ChromeDriverFactory CLASS LOADED FROM: {}",
                ChromeDriverFactory.class.getProtectionDomain().getCodeSource().getLocation());
        LOG.warn("ChromeDriverFactory - USING OptionsFactory.chrome() ???");

        LOG.info("[Chrome] Initializing LOCAL ChromeDriver...");

        try {
            WebDriverManager.chromedriver().setup();
            LOG.debug("[Chrome] WebDriverManager setup completed.");

            ChromeOptions options = OptionsFactory.chrome();
            LOG.debug("[Chrome] Loaded ChromeOptions");

            return new ChromeDriver(options);

        } catch (Exception e) {
            LOG.error("[Chrome] Failed to initialize LOCAL ChromeDriver", e);
            throw new RuntimeException("Failed to initialize local ChromeDriver", e);
        }
    }

    @Override
    public WebDriver createRemoteDriver(URL gridUrl) {
        LOG.info("[Chrome] Initializing REMOTE ChromeDriver => {}", gridUrl);

        try {
            ChromeOptions options = OptionsFactory.chrome();
            LOG.debug("[Chrome] Loaded ChromeOptions for REMOTE");

            return new RemoteWebDriver(gridUrl, options);

        } catch (Exception e) {
            LOG.error("[Chrome] Failed to initialize REMOTE ChromeDriver", e);
            throw new RuntimeException("Failed to initialize remote ChromeDriver", e);
        }
    }
}
