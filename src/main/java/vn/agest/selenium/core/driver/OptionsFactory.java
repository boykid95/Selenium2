package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import vn.agest.selenium.core.config.ConfigLoader;

public class OptionsFactory {

    private static final Logger LOG = LogManager.getLogger(OptionsFactory.class);

    static {
        System.err.println("### OptionsFactory CLASS LOADED FROM: "
                + OptionsFactory.class.getProtectionDomain().getCodeSource().getLocation());
    }

    private static boolean isHeadless() {
        LOG.warn("HEADLESS CHECK = cli:'{}'  final:'{}'",
                System.getProperty("headless"),
                Boolean.parseBoolean(System.getProperty("headless", ConfigLoader.getString("headless"))));

        String cliValue = System.getProperty("headless");
        if (cliValue != null) {
            LOG.info("[OptionsFactory] Headless detected from CLI: {}", cliValue);
            return Boolean.parseBoolean(cliValue);
        }

        boolean value = ConfigLoader.getBoolean("headless");
        LOG.info("[OptionsFactory] Headless from config.json: {}", value);
        return value;
    }

    public static ChromeOptions chrome() {
        LOG.warn("OptionsFactory Loaded From: {}", OptionsFactory.class.getProtectionDomain().getCodeSource().getLocation());

        ChromeOptions options = new ChromeOptions();

        LOG.info("Building Chrome Options...");
        options.setAcceptInsecureCerts(true);

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");

        LOG.error("### FINAL HEADLESS DECISION = {}", isHeadless());

        // HEADLESS
        if (isHeadless()) {
            LOG.info("Chrome running in HEADLESS mode");
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            LOG.info("Chrome running in NORMAL mode");
        }

        return options;
    }

    public static FirefoxOptions firefox() {
        FirefoxOptions options = new FirefoxOptions();

        LOG.info("Building Firefox Options...");
        options.setAcceptInsecureCerts(true);

        if (isHeadless()) {
            LOG.info("Firefox running in HEADLESS mode");
            options.addArguments("-headless");
        } else {
            LOG.info("Firefox running in NORMAL mode");
        }

        return options;
    }

    public static EdgeOptions edge() {
        EdgeOptions options = new EdgeOptions();

        LOG.info("Building Edge Options...");
        options.setAcceptInsecureCerts(true);

        if (isHeadless()) {
            LOG.info("Edge running in HEADLESS mode");
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            LOG.info("Edge running in NORMAL mode");
        }

        return options;
    }
}
