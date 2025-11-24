package vn.agest.selenium.elements;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import vn.agest.selenium.core.config.SystemConfig;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.locator.LocatorFactory;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.enums.Condition;

public class BaseElement {

    private static final Logger LOG = LoggerManager.getLogger(BaseElement.class);

    protected final By locator;
    protected final String name;

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
        By by = LocatorFactory.x(template, args);
        return new BaseElement(by);
    }

    public static BaseElement css(String template, Object... args) {
        By by = LocatorFactory.css(template, args);
        return new BaseElement(by);
    }

    public static By byXPath(String template, Object... args) {
        return LocatorFactory.x(template, args);
    }

    public static By byCss(String template, Object... args) {
        return LocatorFactory.css(template, args);
    }

    // ---------------- Internal Helpers ----------------

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected WebElement find() {
        return driver().findElement(locator);
    }

    protected WebElement findSafe() {
        WebElement el = shouldBe(Condition.VISIBLE);
        if (el == null) {
            LOG.debug("[findSafe] fallback find() → {}", locator);
            return find();
        }
        return el;
    }

    public WebElement shouldBe(Condition... conditions) {
        WebElement el = null;
        for (Condition c : conditions) {
            el = ConditionExecutor.handle(locator, c);
            if (el == null)
                LOG.debug("[CONDITION FAILED] {} -> {}", c, locator);
            return null;
        }
        return el;
    }

    // ---------------- Actions ----------------

    @Step("Click on element: {this.name}")
    public void click() {
        LOG.debug("[CLICK] {}", name);
        try {
            WebElement el = shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
            if (el == null) el = find();

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
            WebElement el = shouldBe(Condition.VISIBLE);
            if (el == null) el = find();

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

        WebElement el = shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        if (el == null) el = find();

        highlight(el);
        el.clear();
        el.sendKeys(text);
    }

    public boolean isDisplayed() {
        try {
            return shouldBe(Condition.VISIBLE) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled() {
        try {
            WebElement el = shouldBe(Condition.PRESENT);
            return el != null && el.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Scroll into view: {this.name}")
    public void scrollTo() {
        WebElement el = shouldBe(Condition.VISIBLE);
        if (el == null) el = find();

        highlight(el);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView(true)", el);
    }

    @Step("Move mouse to: {this.name}")
    public void moveTo() {
        WebElement el = shouldBe(Condition.VISIBLE);
        if (el == null) el = find();

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
