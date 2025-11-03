package vn.agest.selenium.core;

import org.openqa.selenium.WebElement;

public class BaseElement {
    protected WebElement element;

    public BaseElement(WebElement element) {
        this.element = element;
    }

    public void click() {
        element.click();
    }

    public void sendKeys(String text) {
        element.sendKeys(text);
    }

    public String getText() {
        return element.getText();
    }
}
