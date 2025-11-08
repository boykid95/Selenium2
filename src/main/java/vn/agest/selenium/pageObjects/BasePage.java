package vn.agest.selenium.pageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.utils.ElementUtils;

public abstract class BasePage {

    private static final Logger LOG = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    public BasePage(String url) {
        this();
        openUrl(url);
    }

    // ================== NAVIGATION ================== //

    public void openUrl(String url) {
        LOG.info("Opening URL: {}", url);
        driver.get(url);
        waitForPageLoaded();
        LOG.info("Page loaded: {}", driver.getTitle());
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    // ================== ELEMENT ACTIONS ================== //

    public void click(BaseElement element) {
        LOG.debug("Clicking element: {}", element.getLocator());
        element.click();
    }

    public void typeText(BaseElement element, String text) {
        LOG.debug("Typing into {} value='{}'", element.getLocator(), text);
        element.setText(text);
    }

    public String readText(BaseElement element) {
        LOG.debug("Reading text from {}", element.getLocator());
        return element.getText();
    }

    // ================== WAIT METHODS ================== //

    public void waitForPageLoaded() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        LOG.debug("Waiting for page to fully load...");

        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(100);
                if ("complete".equals(js.executeScript("return document.readyState"))) {
                    LOG.debug("Document readyState = complete");
                    return;
                }
            } catch (Exception ignored) {
            }
        }
        LOG.warn("Page did not fully load after timeout.");
    }

    public void waitForUrlContains(String partialUrl) {
        LOG.debug("Waiting for URL to contain: {}", partialUrl);

        for (int i = 0; i < 30; i++) {
            if (driver.getCurrentUrl().contains(partialUrl)) {
                LOG.debug("URL matched: {}", driver.getCurrentUrl());
                return;
            }
            sleep(100);
        }
        throw new RuntimeException("URL does not contain: " + partialUrl);
    }

    public void waitForTitle(String expectedTitle) {
        LOG.debug("Waiting for title to be: {}", expectedTitle);

        for (int i = 0; i < 30; i++) {
            if (driver.getTitle().equals(expectedTitle)) {
                LOG.debug("Title matched: {}", expectedTitle);
                return;
            }
            sleep(100);
        }
        throw new RuntimeException("Title does not match: " + expectedTitle);
    }

    // ================== SCROLL HELPERS ================== //

    public void scrollToElement(BaseElement element) {
        LOG.debug("Scrolling to element: {}", element.getLocator());
        ElementUtils.scrollIntoView(element.getLocator());
    }

    public void scrollDown() {
        LOG.debug("Scrolling down 300px");
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300)");
    }

    // ================== UTILITY ================== //

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
