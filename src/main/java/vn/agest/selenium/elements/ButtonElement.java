package vn.agest.selenium.elements;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ButtonElement extends BaseElement {

    public ButtonElement(By locator) {
        super(locator, "Button: " + locator);
    }

    public ButtonElement(By locator, String name) {
        super(locator, name);
    }

    @Step("Click button: {this.name}")
    @Override
    public void click() {
        super.click();
    }

    @Step("JS Click button: {this.name}")
    @Override
    public void jsClick() {
        super.jsClick();
    }
}
