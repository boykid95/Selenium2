package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import vn.agest.selenium.core.driver.DriverManager;

import java.time.Duration;
import java.util.function.Function;

public final class WaitHelper {

    private static final Logger LOG = LogManager.getLogger(WaitHelper.class);

    private static final int EXPLICIT_TIMEOUT =
            Integer.parseInt(System.getProperty("explicitTimeout", "10"));

    private WaitHelper() {
    }

    private static FluentWait<WebDriver> getWait() {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(EXPLICIT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
    }

    // =============== Internal Wrapper ===============
    private static <T> T wrap(By locator, Function<WebDriver, T> condition, String label) {
        try {
            LOG.debug("[WAIT:{}] {}", label, locator);
            return getWait().until(condition);
        } catch (TimeoutException e) {
            LOG.error("[TIMEOUT:{}] {}", label, locator);
            throw e;
        } catch (Exception e) {
            LOG.error("[WAIT ERROR:{}] {}", label, locator, e);
            throw e;
        }
    }

    // =============== BASIC WAITS ===============

    @Step("Wait for VISIBLE: {locator}")
    public static WebElement waitForVisible(By locator) {
        return wrap(locator,
                ExpectedConditions.visibilityOfElementLocated(locator),
                "VISIBLE");
    }

    @Step("Wait for CLICKABLE: {locator}")
    public static WebElement waitForClickable(By locator) {
        return wrap(locator,
                ExpectedConditions.elementToBeClickable(locator),
                "CLICKABLE");
    }

    @Step("Wait for PRESENCE: {locator}")
    public static WebElement waitPresence(By locator) {
        return wrap(locator,
                ExpectedConditions.presenceOfElementLocated(locator),
                "PRESENCE");
    }

    @Step("Wait for INVISIBLE: {locator}")
    public static boolean waitForInvisible(By locator) {
        return wrap(locator,
                ExpectedConditions.invisibilityOfElementLocated(locator),
                "INVISIBLE");
    }

    // =============== ADVANCED WAITS ===============

    @Step("Wait for TEXT '{text}' in: {locator}")
    public static boolean waitForText(By locator, String text) {
        return wrap(locator,
                ExpectedConditions.textToBePresentInElementLocated(locator, text),
                "TEXT");
    }

    @Step("Wait for URL contains: {fragment}")
    public static boolean waitForUrlContains(String fragment) {
        return getWait().until(ExpectedConditions.urlContains(fragment));
    }

    @Step("Wait for ATTRIBUTE {attribute} contains '{value}' in {locator}")
    public static boolean waitForAttribute(By locator, String attr, String value) {
        return wrap(locator,
                ExpectedConditions.attributeContains(locator, attr, value),
                "ATTRIBUTE");
    }

    @Step("Wait for element COUNT = {expected}")
    public static boolean waitForElementCount(By locator, int expected) {
        return wrap(locator,
                driver -> driver.findElements(locator).size() == expected,
                "ELEMENT-COUNT");
    }

    @Step("Wait for PAGE LOAD complete")
    public static void waitForPageLoaded() {
        getWait().until(driver ->
                ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    @Step("Wait visible & clickable: {locator}")
    public static WebElement waitForVisibleAndClickable(By locator) {
        waitForVisible(locator);
        return waitForClickable(locator);
    }
}
