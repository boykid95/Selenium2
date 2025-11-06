package vn.agest.selenium.pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.Log;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.utils.ElementUtils;

public abstract class BasePage {

    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    public BasePage(String url) {
        this();
        openUrl(url);
    }

    // =============== NAVIGATION =============== //

    public void openUrl(String url) {
        Log.info("Opening URL: " + url);
        driver.get(url);
        waitForPageLoaded();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    // =============== ELEMENT ACTIONS =============== //

    public void click(BaseElement element) {
        element.click();
    }

    public void typeText(BaseElement element, String text) {
        element.setText(text);
    }

    public String readText(BaseElement element) {
        return element.getText();
    }

    // =============== WAIT METHODS =============== //

    public void waitForPageLoaded() {
        Log.info("Waiting for page to load...");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(100);
                if ("complete".equals(js.executeScript("return document.readyState"))) {
                    return;
                }
            } catch (Exception ignored) {
            }
        }

        Log.warn("Page did not fully load after timeout.");
    }

    public void waitForUrlContains(String partialUrl) {
        for (int i = 0; i < 30; i++) {
            if (driver.getCurrentUrl().contains(partialUrl)) return;
            sleep(100);
        }
        throw new RuntimeException("URL does not contain: " + partialUrl);
    }

    public void waitForTitle(String expectedTitle) {
        for (int i = 0; i < 30; i++) {
            if (driver.getTitle().equals(expectedTitle)) return;
            sleep(100);
        }
        throw new RuntimeException("Title does not match: " + expectedTitle);
    }

    // =============== SCROLL HELPERS =============== //

    public void scrollToElement(BaseElement element) {
        ElementUtils.scrollIntoView(element.getLocator());
    }

    public void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300)");
    }

    // =============== UTILITY =============== //

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
