package vn.agest.selenium.elements;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import vn.agest.selenium.core.config.SystemConfig;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.utils.WaitHelper;

public class BaseElement {

    private static final Logger LOG = LogManager.getLogger(BaseElement.class);

    protected final By locator;
    protected final String name;   // readable name for logging + Allure

    // ---------------- Constructors ----------------

    public BaseElement(By locator) {
        this(locator, locator.toString());
    }

    public BaseElement(By locator, String name) {
        this.locator = locator;
        this.name = name;
    }

    // ---------------- Factory Methods ----------------

    public static BaseElement xpath(String template, Object... args) {
        return new BaseElement(By.xpath(String.format(template, args)));
    }

    public static BaseElement css(String template, Object... args) {
        return new BaseElement(By.cssSelector(String.format(template, args)));
    }

    // ---------------- Internal Helpers ----------------

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected WebElement find() {
        return driver().findElement(locator);
    }

    protected WebElement findSafe() {
        return WaitHelper.waitForVisible(locator);
    }

    // ---------------- Actions ----------------

    @Step("Click on element: {this.name}")
    public void click() {
        LOG.info("[CLICK] {}", name);
        try {
            WebElement el = WaitHelper.waitForClickable(locator);
            highlight(el);
            el.click();
        } catch (Exception ex) {
            LOG.error("[CLICK FAILED] {} → fallback to JS click", name);
            jsClick();
        }
    }

    @Step("JS Click on element: {this.name}")
    public void jsClick() {
        try {
            WebElement el = findSafe();
            highlight(el);
            ((JavascriptExecutor) driver()).executeScript("arguments[0].click()", el);
        } catch (Exception e) {
            LOG.error("[JS CLICK FAILED] {}", name, e);
            throw new RuntimeException("JS click failed: " + name, e);
        }
    }

    @Step("Get text of: {this.name}")
    public String getText() {
        return findSafe().getText();
    }

    @Step("Set text '{text}' into: {this.name}")
    public void setText(String text) {
        LOG.info("[SET TEXT] {} → '{}'", name, text);
        WebElement el = findSafe();
        highlight(el);
        el.clear();
        el.sendKeys(text);
    }

    public boolean isDisplayed() {
        try {
            return findSafe().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled() {
        try {
            return findSafe().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Scroll into view: {this.name}")
    public void scrollTo() {
        WebElement el = findSafe();
        highlight(el);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView(true)", el);
    }

    @Step("Move mouse to: {this.name}")
    public void moveTo() {
        WebElement el = findSafe();
        highlight(el);
        new Actions(driver()).moveToElement(el).perform();
    }

    protected void highlight(WebElement element) {
        boolean enabled = SystemConfig.getBoolean("highlight.enabled", true);
        if (!enabled) return;

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver();
            js.executeScript("arguments[0].style.border='2px solid #ff0000'", element);
            Thread.sleep(100);
            js.executeScript("arguments[0].style.border=''", element);
        } catch (Exception ignore) {
        }
    }

    // ---------------- Exposure methods ----------------

    public By getLocator() {
        return locator;
    }

    public String getName() {
        return name;
    }
}
