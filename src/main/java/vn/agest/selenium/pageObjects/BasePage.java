package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.config.ConfigReader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;

public abstract class BasePage {

    private static final Logger LOG = LogManager.getLogger(BasePage.class);
    protected final WebDriver driver;
    protected final PageType pageType;

    public BasePage(PageType pageType) {
        this.pageType = pageType;
        this.driver = DriverManager.getDriver();
    }

    @Step("Open URL: {url}")
    public void open() {
        String url = ConfigReader.getUrlFromPageType(pageType);
        LOG.info("🌐 Opening [{}] → {}", pageType.name(), url);
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

    @Step("Verify expected page title")
    public void verifyPageTitle() {
        String expected = ConfigReader.getTitleFromPageType(pageType);
        String actual = DriverManager.getDriver().getTitle();

        LOG.info("EXPECTED title = {}", expected);
        LOG.info("ACTUAL title   = {}", actual);

        if (!actual.contains(expected)) {
            throw new AssertionError(
                    "Page title mismatch.\nExpected: " + expected + "\nActual: " + actual
            );
        }
    }

    protected BaseElement $x(String template, Object... args) {
        return BaseElement.xpath(template, args);
    }

    protected BaseElement $c(String template, Object... args) {
        return BaseElement.css(template, args);
    }
}
