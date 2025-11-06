package vn.agest.selenium.elements;

import org.openqa.selenium.By;

public class TextBox extends BaseElement {

    public TextBox(By locator) {
        super(locator);
    }

    public void type(String text) {
        setText(text);
    }
}
