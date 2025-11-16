package vn.agest.selenium.elements;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.config.SystemConfig;
import vn.agest.selenium.core.driver.DriverManager;

import java.time.Duration;

public class BaseElement {

    private static final Logger LOG = LogManager.getLogger(BaseElement.class);
    protected final By locator;

    public BaseElement(By locator) {
        this.locator = locator;
    }

    // Dynamic factory
    public static BaseElement xpath(String template, Object... args) {
        return new BaseElement(By.xpath(String.format(template, args)));
    }

    public static BaseElement css(String template, Object... args) {
        return new BaseElement(By.cssSelector(String.format(template, args)));
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected WebDriverWait waitUi() {
        long timeout = ConfigLoader.getInt("explicitTimeout");
        return new WebDriverWait(driver(), Duration.ofSeconds(timeout));
    }

    protected WebElement find() {
        return driver().findElement(locator);
    }

    protected WebElement waitForVisible() {
        LOG.debug("[Wait Visible] {}", locator);
        return waitUi().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable() {
        LOG.debug("[Wait Clickable] {}", locator);
        return waitUi().until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Click element: {this.locator}")
    public void click() {
        LOG.info("[Click] {}", locator);
        try {
            WebElement e = waitForClickable();
            highlight(e);
            e.click();
        } catch (Exception ex) {
            LOG.warn("[Click] Failed → fallback JS click → {}", locator);
            jsClick();
        }
    }

    public void jsClick() {
        LOG.info("[JS Click] {}", locator);
        try {
            WebElement e = waitForVisible();
            highlight(e);
            ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", e);
        } catch (Exception ex) {
            LOG.error("[JS Click] FAILED → {}", locator, ex);
            throw new RuntimeException("JS click failed for: " + locator, ex);
        }
    }

    @Step("Get text from element: {this.locator}")
    public String getText() {
        LOG.debug("[GetText] {}", locator);
        return waitForVisible().getText();
    }

    @Step("Set text '{text}' into: {this.locator}")
    public void setText(String text) {
        LOG.info("[Type] {} => '{}'", locator, text);
        WebElement e = waitForVisible();
        highlight(e);
        e.clear();
        e.sendKeys(text);
    }

    public boolean isDisplayed() {
        try {
            return waitForVisible().isDisplayed();
        } catch (Exception ex) {
            LOG.warn("[Displayed?] FALSE: {}", locator);
            return false;
        }
    }

    public boolean isEnabled() {
        try {
            return waitForVisible().isEnabled();
        } catch (Exception ex) {
            LOG.warn("[Enabled?] FALSE: {}", locator);
            return false;
        }
    }

    public void scrollTo() {
        LOG.info("[ScrollIntoView] {}", locator);
        WebElement e = waitForVisible();
        highlight(e);
        ((JavascriptExecutor) driver())
                .executeScript("arguments[0].scrollIntoView(true);", e);
    }

    public void moveTo() {
        LOG.info("[MoveTo] {}", locator);
        WebElement e = waitForVisible();
        highlight(e);
        new Actions(driver()).moveToElement(e).perform();
    }

    protected void highlight(WebElement element) {
        boolean enabled = SystemConfig.getBoolean("highlight.enabled", true);
        if (!enabled) return;

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver();
            js.executeScript("arguments[0].style.border='2px solid red'", element);
            Thread.sleep(120);
            js.executeScript("arguments[0].style.border=''", element);
        } catch (Exception ignore) {
        }
    }
}
