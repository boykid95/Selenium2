package vn.agest.selenium.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class AllureHelper {

    private static final Logger LOG = LoggerManager.getLogger(AllureHelper.class);

    private AllureHelper() {
    }

    private static WebDriver driver() {
        try {
            return DriverManager.getDriver();
        } catch (Exception e) {
            LOG.error("❌ Could not get WebDriver for Allure attachment", e);
            return null;
        }
    }

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(String name) {
        WebDriver drv = driver();
        if (drv == null) {
            LOG.warn("⚠️ WebDriver is NULL during screenshot capture");
            return "No screenshot available - WebDriver was null.".getBytes(StandardCharsets.UTF_8);
        }

        try {
            LOG.info("📸 Capturing viewport screenshot for step: {}", name);
            return ((TakesScreenshot) drv).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            LOG.error("❌ Failed to capture screenshot '{}'", name, e);
            return ("Error capturing screenshot: " + e.getMessage()).getBytes(StandardCharsets.UTF_8);
        }
    }

    public static void captureWithScrollAndHighlight(WebElement element, String name) {
        WebDriver drv = driver();
        if (drv == null || element == null) {
            LOG.warn("⚠️ WebDriver or element is null during scroll+highlight capture");
            return;
        }

        try {
            ((JavascriptExecutor) drv).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
            WaitHelper.pause(350);

            ((JavascriptExecutor) drv).executeScript(
                    "arguments[0].style.outline='3px solid red'; arguments[0].style.transition='outline 0.2s ease';", element);

            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(drv);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot.getImage(), "png", baos);

            Allure.getLifecycle().addAttachment(name, "image/png", "png", baos.toByteArray());
            LOG.info("📸 Captured fullscreen with highlight for step: {}", name);

        } catch (Exception e) {
            LOG.error("❌ Failed to capture fullscreen with element highlight for '{}': {}", name, e.getMessage());
        } finally {
            try {
                ((JavascriptExecutor) drv).executeScript("arguments[0].style.outline='';", element);
            } catch (Exception ignored) {
            }
        }
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String text) {
        return text == null ? "No text" : text;
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource() {
        WebDriver drv = driver();
        if (drv == null) {
            LOG.warn("⚠️ WebDriver is NULL during page source capture");
            return "Cannot capture page source - WebDriver was null.";
        }

        try {
            LOG.info("📄 Capturing and attaching page source to Allure");
            return drv.getPageSource();
        } catch (Exception e) {
            LOG.error("❌ Failed to capture page source for Allure", e);
            return "Error capturing page source: " + e.getMessage();
        }
    }
}
