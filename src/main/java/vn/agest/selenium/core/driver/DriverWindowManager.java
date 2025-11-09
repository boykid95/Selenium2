package vn.agest.selenium.core.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.config.WindowConfig;

public final class DriverWindowManager {

    private static final Logger LOG = LogManager.getLogger(DriverWindowManager.class);

    private DriverWindowManager() {
    }

    public static void apply(WebDriver driver) {
        if (driver == null) {
            LOG.error("Cannot apply window mode — WebDriver is null.");
            return;
        }

        String mode = WindowConfig.mode();
        if (mode == null || mode.isBlank()) {
            LOG.warn("Window mode is null/empty → fallback to maximize");
            driver.manage().window().maximize();
            return;
        }

        mode = mode.toLowerCase();
        LOG.info("Applying browser window mode: {}", mode);

        switch (mode) {

            case "fullscreen" -> {
                LOG.info("Window = fullscreen");
                driver.manage().window().fullscreen();
            }

            case "custom" -> applyCustomSize(driver);

            default -> {
                LOG.info("Window = maximize");
                driver.manage().window().maximize();
            }
        }
    }

    private static void applyCustomSize(WebDriver driver) {
        int width = WindowConfig.width();
        int height = WindowConfig.height();

        if (width > 0 && height > 0) {
            LOG.info("Window = custom ({}x{})", width, height);
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(new Dimension(width, height));
        } else {
            LOG.warn("Invalid custom size (width={}, height={}) → fallback to maximize",
                    width, height);
            driver.manage().window().maximize();
        }
    }
}
