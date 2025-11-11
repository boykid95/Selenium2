package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.config.ConfigReader;
import vn.agest.selenium.enums.BrowserType;

import java.net.URL;

public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver createInstance(String browser) {

        WebDriverFactory factory = switch (BrowserType.fromString(browser)) {
            case CHROME -> new ChromeDriverFactory();
            case FIREFOX -> new FirefoxDriverFactory();
            case EDGE -> new EdgeDriverFactory();
        };

        boolean isRemote = ConfigReader.getBoolean("remote", false);

        try {
            WebDriver driver = isRemote
                    ? factory.createRemoteDriver(new URL(ConfigReader.get("gridUrl")))
                    : factory.createLocalDriver();

            applyWindowMode(driver);

            LOG.info("WebDriver initialized [{}] (remote={})", browser, isRemote);
            return driver;

        } catch (Exception e) {
            LOG.error("WebDriver creation failed for browser: {}", browser, e);
            throw new RuntimeException(e);
        }
    }

    private static void applyWindowMode(WebDriver driver) {
        String mode = ConfigReader.get("window.mode");
        if (mode == null || mode.isBlank()) {
            mode = "maximize";
        }
        mode = mode.toLowerCase();

        switch (mode) {

            case "fullscreen" -> {
                LOG.info("Window mode = fullscreen");
                driver.manage().window().fullscreen();
            }

            case "custom" -> {
                int width = ConfigReader.getInt("window.width", 1280);
                int height = ConfigReader.getInt("window.height", 800);
                LOG.info("Window mode = custom ({}x{})", width, height);
                driver.manage().window().setSize(new Dimension(width, height));
            }

            default -> {
                LOG.info("Window mode = maximize");
                driver.manage().window().maximize();
            }
        }
    }
}
