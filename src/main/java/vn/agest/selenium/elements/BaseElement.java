package vn.agest.selenium.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.utils.ElementUtils;

public class BaseElement {

    protected By locator;

    public BaseElement(By locator) {
        this.locator = locator;
    }

    // ================== BASIC ACTIONS ================== //

    public void click() {
        ElementUtils.click(locator);
    }

    public String getText() {
        WebElement element = ElementUtils.waitForVisible(locator);
        return element.getText();
    }

    public void setText(String text) {
        ElementUtils.type(locator, text);
    }

    public boolean isDisplayed() {
        try {
            return ElementUtils.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollTo() {
        ElementUtils.scrollIntoView(locator);
    }

    public void highlight() {
        ElementUtils.highlight(locator);
    }
}
