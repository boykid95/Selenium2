package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;

public abstract class BasePage {

    private static final Logger LOG = LogManager.getLogger(BasePage.class);

    protected final WebDriver driver;
    protected final PageType pageType;

    public BasePage(PageType pageType) {
        this.driver = DriverManager.getDriver();
        this.pageType = pageType;
    }

    @Step("Open page: {this.pageType}")
    public void open() {
        String url = ConfigLoader.getPageUrl(pageType);
        LOG.info("Opening [{}] → {}", pageType.name(), url);

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
                Thread.sleep(120);
            } catch (Exception ignore) {
            }
        }

        LOG.warn("Page load timeout exceeded → may not be fully loaded.");
    }

    @Step("Get expected title of current page")
    public String getExpectedTitle() {
        String expectedTitle = ConfigLoader.getPageTitle(pageType);
        LOG.info("Expected page title for {}: {}", pageType, expectedTitle);
        return expectedTitle;
    }

    @Step("Get current page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        LOG.info("Current page title: {}", title);
        return title;
    }

    @Step("Verify page title for {this.pageType}")
    public void verifyPageTitle() {
        String expected = ConfigLoader.getPageTitle(pageType);
        String actual = driver.getTitle();

        LOG.info("Expected title = {}", expected);
        LOG.info("Actual title   = {}", actual);

        if (!actual.contains(expected)) {
            throw new AssertionError(
                    "Page title mismatch.\nExpected: " + expected + "\nActual: " + actual
            );
        }

        LOG.info("✔ Page title verified successfully!");
    }

    protected BaseElement $x(String template, Object... args) {
        return BaseElement.xpath(template, args);
    }

    protected BaseElement $c(String template, Object... args) {
        return BaseElement.css(template, args);
    }
}
