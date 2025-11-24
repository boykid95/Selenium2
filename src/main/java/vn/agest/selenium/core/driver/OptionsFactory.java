package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.log.LoggerManager;

public class OptionsFactory {

    private static final Logger LOG = LoggerManager.getLogger(OptionsFactory.class);

    static {
        LOG.debug("OptionsFactory loaded from: {}",
                OptionsFactory.class.getProtectionDomain().getCodeSource().getLocation());
    }

    private static boolean isHeadless() {
        String cliValue = System.getProperty("headless");

        if (cliValue != null) {
            boolean parsed = Boolean.parseBoolean(cliValue);
            LOG.debug("[OptionsFactory] Headless overridden via CLI → {}", parsed);
            return parsed;
        }

        boolean configValue = ConfigLoader.getBoolean("headless");
        LOG.debug("[OptionsFactory] Headless from config.json → {}", configValue);
        return configValue;
    }

    public static ChromeOptions chrome() {
        LOG.info("Building Chrome Options...");
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);

        options.addArguments("--remote-allow-origins=*");

        if (isHeadless()) {
            LOG.info("Chrome running in HEADLESS mode");
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            LOG.debug("Chrome running in normal mode");
        }

        return options;
    }

    public static FirefoxOptions firefox() {
        LOG.info("Building Firefox Options...");
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);

        if (isHeadless()) {
            LOG.info("Firefox running in HEADLESS mode");
            options.addArguments("-headless");
        } else {
            LOG.debug("Firefox running in normal mode");
        }

        return options;
    }

    public static EdgeOptions edge() {
        LOG.info("Building Edge Options...");
        EdgeOptions options = new EdgeOptions();
        options.setAcceptInsecureCerts(true);

        options.addArguments("--remote-allow-origins=*");

        if (isHeadless()) {
            LOG.info("Edge running in HEADLESS mode");
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            LOG.debug("Edge running in normal mode");
        }

        return options;
    }
}
