package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import vn.agest.selenium.core.config.ConfigLoader;

public class OptionsFactory {

    private static final Logger LOG = LogManager.getLogger(OptionsFactory.class);

    // ======================== CHROME ========================
    public static ChromeOptions chrome() {
        ChromeOptions options = new ChromeOptions();

        LOG.info("Building Chrome Options...");
        options.setAcceptInsecureCerts(true);
        LOG.debug("Set accept insecure certs = true");

        options.addArguments("--disable-notifications");
        LOG.debug("Added argument: --disable-notifications");

        if (ConfigLoader.getBoolean("headless")) {
            options.addArguments("--headless=new");
            LOG.info("Chrome running in HEADLESS mode");
        } else {
            LOG.info("Chrome running in NORMAL mode");
        }

        return options;
    }

    // ======================== FIREFOX ========================
    public static FirefoxOptions firefox() {
        FirefoxOptions options = new FirefoxOptions();

        LOG.info("Building Firefox Options...");
        options.setAcceptInsecureCerts(true);
        LOG.debug("Set accept insecure certs = true");

        if (ConfigLoader.getBoolean("headless")) {
            options.addArguments("-headless");
            LOG.info("Firefox running in HEADLESS mode");
        } else {
            LOG.info("Firefox running in NORMAL mode");
        }

        return options;
    }

    // ======================== EDGE ========================
    public static EdgeOptions edge() {
        EdgeOptions options = new EdgeOptions();

        LOG.info("Building Edge Options...");
        options.setAcceptInsecureCerts(true);
        LOG.debug("Set accept insecure certs = true");

        if (ConfigLoader.getBoolean("headless")) {
            options.addArguments("--headless=new");
            LOG.info("Edge running in HEADLESS mode");
        } else {
            LOG.info("Edge running in NORMAL mode");
        }

        return options;
    }
}
