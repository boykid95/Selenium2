package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.elements.BaseElement;

public abstract class BasePage {

    private static final Logger LOG = LogManager.getLogger(BasePage.class);
    protected final WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    @Step("Open URL: {url}")
    public void open(String url) {
        LOG.info("Opening URL: {}", url);
        driver.get(url);
        waitForPageLoaded();
    }

    @Step("Wait for page fully loaded")
    public void waitForPageLoaded() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < 30; i++) {
            try {
                if ("complete".equals(js.executeScript("return document.readyState")))
                    return;
                Thread.sleep(100);
            } catch (Exception ignore) {
            }
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    protected BaseElement $x(String template, Object... args) {
        return BaseElement.xpath(template, args);
    }

    protected BaseElement $c(String template, Object... args) {
        return BaseElement.css(template, args);
    }
}
