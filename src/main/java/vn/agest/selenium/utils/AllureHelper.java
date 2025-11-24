package vn.agest.selenium.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;

import java.nio.charset.StandardCharsets;

public final class AllureHelper {

    private static final Logger LOG = LoggerManager.getLogger(AllureHelper.class);

    private AllureHelper() {
    }

    private static WebDriver driver() {
        try {
            return DriverManager.getDriver();
        } catch (Exception e) {
            LOG.error("Could not get WebDriver for Allure attachment", e);
            return null;
        }
    }

    public static void attachScreenshot(String name) {
        try {
            WebDriver drv = driver();
            if (drv == null) {
                LOG.warn("WebDriver is NULL during screenshot capture");
                attachText(name + " (fallback)", "Screenshot not available - WebDriver was null.");
                return;
            }

            byte[] screenshot = ((TakesScreenshot) drv).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment(name, "image/png", "png", screenshot);

        } catch (Exception e) {
            LOG.error("Failed to attach screenshot to Allure", e);
            attachText(name + " (fallback)", "Error capturing screenshot: " + e.getMessage());
        }
    }

    public static void attachText(String name, String text) {
        try {
            Allure.getLifecycle().addAttachment(
                    name,
                    "text/plain",
                    "txt",
                    text.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            LOG.error("Failed to attach text to Allure", e);
        }
    }

    public static void attachPageSource() {
        try {
            WebDriver drv = driver();
            if (drv == null) {
                LOG.warn("WebDriver is NULL during page source capture");
                attachText("Page Source (fallback)", "Cannot capture page source - WebDriver was null.");
                return;
            }

            String src = drv.getPageSource();
            attachText("Page Source", src);

        } catch (Exception e) {
            LOG.error("Failed to attach page source to Allure", e);
            attachText("Page Source (fallback)", "Error capturing page source: " + e.getMessage());
        }
    }
}
