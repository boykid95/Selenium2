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

    // ================= CORE WAIT FACTORIES =================

    private static WebDriverWait waitDefault() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(ConfigLoader.timeout("explicit", 10))
        );
    }

    private static WebDriverWait waitShort() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(ConfigLoader.timeout("short", 3))
        );
    }

    private static WebDriverWait waitLong() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(ConfigLoader.timeout("long", 20))
        );
    }

    // ================= BASIC WAITS =================

    @Step("Wait for VISIBLE: {locator} (timeout={timeout}s)")
    public static WebElement waitForVisible(By locator, int timeout) {
        LOG.debug("[WAIT:{}s] visible → {}", timeout, locator);
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

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

    // ================= INVISIBLE =================

    @Step("Wait for INVISIBLE: {locator} (timeout={timeout}s)")
    public static void waitForInvisible(By locator, int timeout) {
        LOG.debug("[WAIT:{}s] invisible → {}", timeout, locator);
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Step("Wait for INVISIBLE: {locator}")
    public static void waitForInvisible(By locator) {
        waitForInvisible(locator, ConfigLoader.timeout("explicit", 10));
    }

    // ================= ADVANCED WAITS =================

    @Step("Wait for TEXT '{text}' in {locator}")
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

    // ================= SHORTCUTS =================

    @Step("Short wait for VISIBLE: {locator}")
    public static WebElement shortVisible(By locator) {
        LOG.debug("[WAIT-SHORT] visible → {}", locator);
        return waitShort().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Long wait for VISIBLE: {locator}")
    public static WebElement longVisible(By locator) {
        LOG.debug("[WAIT-LONG] visible → {}", locator);
        return waitLong().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Long wait for INVISIBLE: {locator}")
    public static void longInvisible(By locator) {
        LOG.debug("[WAIT-LONG] invisible → {}", locator);
        waitLong().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ================= UTILITY =================
    // will be removed in future
    public static void pause(long millis) {
        try {
            LOG.debug("[PAUSE] {} ms", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Step("Wait for overlay appear and disappear: {locator} (appear={appearTimeout}s, disappear={disappearTimeout}s)")
    public static void waitForAppearAndDisappear(By locator, int appearTimeout, int disappearTimeout) {
        LOG.debug("[WAIT] appear-disappear → {} (appear={}, disappear={})", locator, appearTimeout, disappearTimeout);

        try {
            WebDriverWait waitAppear = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(appearTimeout));
            WebDriverWait waitDisappear = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(disappearTimeout));

            waitAppear.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(locator),
                    ExpectedConditions.invisibilityOfElementLocated(locator)
            ));

            waitDisappear.until(ExpectedConditions.invisibilityOfElementLocated(locator));

            LOG.debug("✅ Overlay appeared and disappeared successfully: {}", locator);
        } catch (Exception e) {
            LOG.warn("⚠️ Overlay appear/disappear wait skipped or timed out for {}: {}", locator, e.getMessage());
        }
    }

    @Step("Wait for overlay appear and disappear: {locator}")
    public static void waitForAppearAndDisappear(By locator) {
        int appear = ConfigLoader.timeout("short");
        int disappear = ConfigLoader.timeout("long");
        waitForAppearAndDisappear(locator, appear, disappear);
    }
}
