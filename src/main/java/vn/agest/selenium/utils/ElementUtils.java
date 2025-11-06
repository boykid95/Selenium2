package vn.agest.selenium.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.agest.selenium.core.config.ConfigReader;
import vn.agest.selenium.core.driver.DriverManager;

import java.time.Duration;

public class ElementUtils {

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
        return uiWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        return uiWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ===================== ACTION METHODS ===================== //

    public static void click(By locator) {
        try {
            highlight(locator);
            waitForClickable(locator).click();
        } catch (Exception e) {
            jsClick(locator); // fallback
        }
    }

    public static void jsClick(By locator) {
        try {
            WebElement element = waitForVisible(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver();
            highlight(locator);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new RuntimeException("JS click failed for: " + locator, e);
        }
    }

    public static void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        highlight(locator);
        element.clear();
        element.sendKeys(text);
    }

    // ===================== SUPPORT METHODS ===================== //

    public static void scrollIntoView(By locator) {
        WebElement element = waitForVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver();
        highlight(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void highlight(By locator) {
        WebElement element = waitForVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("arguments[0].style.border='3px solid red'", element);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
        }
        js.executeScript("arguments[0].style.border=''", element);
    }

    public static void moveTo(By locator) {
        WebElement element = waitForVisible(locator);
        highlight(locator);
        new Actions(driver()).moveToElement(element).perform();
    }

}
