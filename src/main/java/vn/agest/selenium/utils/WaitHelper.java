package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;

import java.time.Duration;

public final class WaitHelper {

    private static final Logger LOG = LoggerManager.getLogger(WaitHelper.class);

    private WaitHelper() {
    }

    // ================= CORE WAIT =================
    private static WebDriverWait waitDefault() {
        int timeout = ConfigLoader.timeout("explicit");
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
    }

    // ================= BASIC WAITS =================

    @Step("Wait for VISIBLE: {locator}")
    public static WebElement waitForVisible(By locator) {
        LOG.debug("[WAIT] visible → {}", locator);
        return waitDefault().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Wait for CLICKABLE: {locator}")
    public static WebElement waitForClickable(By locator) {
        LOG.debug("[WAIT] clickable → {}", locator);
        return waitDefault().until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Wait for PRESENCE: {locator}")
    public static WebElement waitForPresence(By locator) {
        LOG.debug("[WAIT] presence → {}", locator);
        return waitDefault().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @Step("Wait for INVISIBLE: {locator}")
    public static void waitForInvisible(By locator) {
        LOG.debug("[WAIT] invisible → {}", locator);
        waitDefault().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static WebDriverWait waitShort() {
        return new WebDriverWait(DriverManager.getDriver(),
                Duration.ofSeconds(ConfigLoader.timeout("short")));
    }

    @Step("Short wait for VISIBLE: {locator}")
    public static WebElement waitShortVisible(By locator) {
        LOG.debug("[WAIT-SHORT] visible → {}", locator);
        return waitShort().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ================= ADVANCED WAITS =================

    @Step("Wait for TEXT '{text}' in: {locator}")
    public static void waitForText(By locator, String text) {
        LOG.debug("[WAIT] text '{}' → {}", text, locator);
        waitDefault().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Step("Wait for URL contains: {fragment}")
    public static void waitForUrlContains(String fragment) {
        LOG.debug("[WAIT] url contains '{}'", fragment);
        waitDefault().until(ExpectedConditions.urlContains(fragment));
    }

    @Step("Wait for ATTRIBUTE {attribute} contains '{value}' in {locator}")
    public static void waitForAttribute(By locator, String attribute, String value) {
        LOG.debug("[WAIT] attribute '{}' contains '{}' → {}", attribute, value, locator);
        waitDefault().until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    @Step("Wait for element COUNT = {expected}")
    public static void waitForElementCount(By locator, int expected) {
        LOG.debug("[WAIT] element-count = {} → {}", expected, locator);
        waitDefault().until(driver -> driver.findElements(locator).size() == expected);
    }

    @Step("Wait visible & clickable: {locator}")
    public static WebElement waitForVisibleAndClickable(By locator) {
        LOG.debug("[WAIT] visible & clickable → {}", locator);
        waitForVisible(locator);
        return waitForClickable(locator);
    }
}
