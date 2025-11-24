package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import vn.agest.selenium.core.config.PageTitleLoader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.PageInfo;

public abstract class BasePage {

    private static final Logger LOG = LoggerManager.getLogger(BasePage.class);

    protected final WebDriver driver;
    protected final PageType pageType;
    protected final PageInfo pageInfo;

    public BasePage(PageType pageType) {
        this.driver = DriverManager.getDriver();
        this.pageType = pageType;
        this.pageInfo = PageTitleLoader.get(pageType);
    }

    // ===================== OPEN PAGE =====================

    @Step("Open page: {this.pageType}")
    public void open() {
        LOG.info("Opening [{}] → {}", pageType.name(), pageInfo.url());
        driver.get(pageInfo.url());
    }

    // ===================== GETTERS =====================

    @Step("Get expected title of current page")
    public String getExpectedTitle() {
        LOG.debug("Expected title for [{}]: {}", pageType, pageInfo.title());
        return pageInfo.title();
    }

    @Step("Get current page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        LOG.debug("Current page title: {}", title);
        return title;
    }

    // ===================== ELEMENT HELPERS =====================

    protected BaseElement $x(String template, Object... args) {
        return BaseElement.xpath(template, args);
    }

    protected BaseElement $c(String template, Object... args) {
        return BaseElement.css(template, args);
    }
}
