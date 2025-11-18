package vn.agest.selenium.elements;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class InputElement extends BaseElement {

    public InputElement(By locator) {
        super(locator, "Input: " + locator);
    }

    public InputElement(By locator, String name) {
        super(locator, name);
    }

    @Step("Set text '{text}' into input: {this.name}")
    @Override
    public void setText(String text) {
        super.setText(text);
    }

    @Step("Clear input: {this.name}")
    public void clear() {
        findSafe().clear();
    }
}
