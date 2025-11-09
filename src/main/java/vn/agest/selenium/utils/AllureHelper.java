package vn.agest.selenium.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.driver.DriverManager;

import java.nio.charset.StandardCharsets;

public final class AllureHelper {

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    public static void attachScreenshot(String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver()).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment(name, "image/png", "png", screenshot);
        } catch (Exception ignored) {
        }
    }

    public static void attachText(String name, String text) {
        Allure.getLifecycle().addAttachment(
                name, "text/plain", "txt",
                text.getBytes(StandardCharsets.UTF_8));
    }

    public static void attachPageSource() {
        try {
            String src = driver().getPageSource();
            attachText("Page Source", src);
        } catch (Exception ignored) {
        }
    }
}
