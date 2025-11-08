package vn.agest.selenium.elements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.utils.ElementUtils;

public class BaseElement {

    private static final Logger LOG = LogManager.getLogger(BaseElement.class);

    private final By locator;

    public BaseElement(By locator) {
        this.locator = locator;
    }

    public By getLocator() {
        return locator;
    }

    // ================== BASIC ACTIONS ================== //

    public void click() {
        LOG.info("Click on element: {}", locator);
        ElementUtils.click(locator);
    }

    public void jsClick() {
        LOG.info("JS Click on element: {}", locator);
        ElementUtils.jsClick(locator);
    }

    public String getText() {
        LOG.debug("Get text from element: {}", locator);
        WebElement element = ElementUtils.waitForVisible(locator);
        return element.getText();
    }

    public void setText(String text) {
        LOG.info("Set text '{}' into element: {}", text, locator);
        ElementUtils.type(locator, text);
    }

    public boolean isDisplayed() {
        LOG.debug("Check if element is displayed: {}", locator);
        try {
            return ElementUtils.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            LOG.warn("Element NOT displayed: {}", locator);
            return false;
        }
    }

    public boolean isEnabled() {
        LOG.debug("Check if element is enabled: {}", locator);
        try {
            return ElementUtils.waitForVisible(locator).isEnabled();
        } catch (Exception e) {
            LOG.warn("Element NOT enabled: {}", locator);
            return false;
        }
    }

    public void scrollTo() {
        LOG.info("Scroll to element: {}", locator);
        ElementUtils.scrollIntoView(locator);
    }

    public void moveTo() {
        LOG.info("Move cursor to element: {}", locator);
        ElementUtils.moveTo(locator);
    }
}
