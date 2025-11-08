package vn.agest.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.agest.selenium.core.config.ConfigReader;
import vn.agest.selenium.core.driver.DriverManager;

import java.time.Duration;

public class ElementUtils {

    private static final Logger LOG = LogManager.getLogger(ElementUtils.class);

    private static final long TIMEOUT =
            ConfigReader.getInt("explicitTimeout", 10);

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    private static WebDriverWait uiWait() {
        return new WebDriverWait(driver(), Duration.ofSeconds(TIMEOUT));
    }

    // ===================== WAIT METHODS ===================== //

    public static WebElement waitForVisible(By locator) {
        LOG.debug("Wait for VISIBLE: {}", locator);
        return uiWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        LOG.debug("Wait for CLICKABLE: {}", locator);
        return uiWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ===================== ACTION METHODS ===================== //

    public static void click(By locator) {
        LOG.info("Click element: {}", locator);
        try {
            WebElement element = waitForClickable(locator);
            highlightElement(element);
            element.click();
        } catch (Exception e) {
            LOG.warn("Normal click failed → fallback to JS click for {}", locator);
            jsClick(locator);
        }
    }

    public static void jsClick(By locator) {
        LOG.info("JS click: {}", locator);
        try {
            WebElement element = waitForVisible(locator);
            highlightElement(element);
            JavascriptExecutor js = (JavascriptExecutor) driver();
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LOG.error("JS click failed for: {}", locator, e);
            throw new RuntimeException("JS click failed for: " + locator, e);
        }
    }

    public static void type(By locator, String text) {
        LOG.info("Type into {} | value='{}'", locator, text);
        WebElement element = waitForVisible(locator);
        highlightElement(element);
        element.clear();
        element.sendKeys(text);
    }

    // ===================== SUPPORT METHODS ===================== //

    public static void scrollIntoView(By locator) {
        LOG.debug("Scroll into view: {}", locator);
        WebElement element = waitForVisible(locator);
        highlightElement(element);
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void moveTo(By locator) {
        LOG.info("Move to element {}", locator);
        WebElement element = waitForVisible(locator);
        highlightElement(element);
        new Actions(driver()).moveToElement(element).perform();
    }

    // ===================== HIGHLIGHT ===================== //

    private static void highlightElement(WebElement element) {
        boolean highlightEnabled =
                Boolean.parseBoolean(System.getProperty("highlight.enabled", "true"));

        if (!highlightEnabled) return;

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver();
            js.executeScript("arguments[0].style.border='3px solid red'", element);
            Thread.sleep(150);
            js.executeScript("arguments[0].style.border=''", element);
        } catch (Exception ignored) {
        }
    }
}
