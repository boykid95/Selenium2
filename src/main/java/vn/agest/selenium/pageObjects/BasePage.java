package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.config.PageTitleLoader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.enums.ProductCategory;
import vn.agest.selenium.model.PageInfo;
import vn.agest.selenium.utils.LocatorHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePage {

    private static final Logger LOG = LoggerManager.getLogger(BasePage.class);

    // ===================== ELEMENT LOCATORS =====================
    protected final BaseElement cookieNoticeBanner = $id("cookie-notice", "Cookie Notice Banner");
    protected final BaseElement cookieAcceptButton = $css("#cookie-notice .cn-set-cookie", "Cookie Accept Button");
    protected final BaseElement popupCloseButton = $css("button.pum-close:nth-child(3)", "Popup Close Button");

    protected final WebDriver driver;
    protected final PageType pageType;
    protected final PageInfo pageInfo;

    // ===================== CONSTRUCTOR =====================
    public BasePage(PageType pageType) {
        this.driver = DriverManager.getDriver();
        this.pageType = pageType;
        if (pageType == null || pageType == PageType.PRODUCT_CATEGORY_PAGE) {
            this.pageInfo = null;
        } else {
            this.pageInfo = PageTitleLoader.get(pageType);
        }
    }

    // ===================== PAGE NAVIGATION =====================

    @Step("Open page: {this.pageType}")
    public void open() {
        LOG.info("Opening [{}] → {}", pageType.name(), pageInfo.url());
        driver.get(pageInfo.url());
    }

    @Step("Navigate to page: {pageType}")
    public void navigateToPage(PageType pageType) {
        PageInfo info = PageTitleLoader.get(pageType);
        LOG.info("Navigating to [{}] → {}", pageType.name(), info.url());
        driver.get(info.url());
    }

    @Step("Navigate to category page: {category.displayName}")
    public void navigateToCategoryPage(ProductCategory category) {
        String fullUrl = PageTitleLoader.get(PageType.PRODUCT_CATEGORY_PAGE).url() + category.getUrlPath();
        LOG.info("🌐 Navigating to Category: {} → {}", category.getDisplayName(), fullUrl);
        driver.get(fullUrl);
    }

    // ===================== PAGE INFO =====================

    @Step("Get expected title of current page")
    public String getExpectedTitle() {
        return pageInfo != null ? pageInfo.title() : "";
    }

    @Step("Get current page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        LOG.debug("Current page title: {}", title);
        return title;
    }

    // ===================== ELEMENT FACTORY =====================

    protected BaseElement $id(String id, String name) {
        return BaseElement.el(By.id(id), name);
    }

    protected BaseElement $css(String cssSelector, String name) {
        return BaseElement.el(By.cssSelector(cssSelector), name);
    }

    protected BaseElement $x(String xpath, String name) {
        return BaseElement.el(By.xpath(xpath), name);
    }

    // ===================== DYNAMIC LOCATOR SUPPORT =====================

    protected BaseElement getDynamicElement(String locatorTemplate, Object... args) {
        return LocatorHelper.getDynamicLocator(locatorTemplate, args);
    }

    // ===================== POPUP HANDLER =====================

    @Step("Close popup if present")
    public void closePopupIfPresent() {
        LOG.info("Checking popup visibility...");

        try {
            WebElement popup = WaitHelper.shortVisible(popupCloseButton.getLocator());
            if (popup != null) {
                popupCloseButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
                popupCloseButton.click();
                LOG.debug("✅ Popup closed successfully.");
            }
        } catch (TimeoutException e) {
            LOG.debug("No popup appeared, continue.");
        } catch (Exception e) {
            LOG.warn("⚠️ Popup handling skipped: {}", e.getMessage());
        }
    }

    // ===================== COOKIE HANDLER =====================

    @Step("Accept cookie notice if visible")
    public void acceptCookieIfVisible() {
        try {
            WebElement banner = WaitHelper.shortVisible(cookieNoticeBanner.getLocator());
            if (banner != null && banner.isDisplayed()) {
                LOG.info("🍪 Cookie notice detected, accepting...");
                cookieAcceptButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
                cookieAcceptButton.click();
                WaitHelper.waitForInvisible(cookieNoticeBanner.getLocator());
                LOG.debug("✅ Cookie notice accepted.");
            } else {
                LOG.debug("No cookie banner detected.");
            }
        } catch (TimeoutException e) {
            LOG.debug("No cookie notice present.");
        } catch (Exception e) {
            LOG.warn("⚠️ Cookie notice handling failed: {}", e.getMessage());
        }
    }

    // ===================== ELEMENT COLLECTION =====================

    @Step("Get all BaseElements for locator: {locator}")
    protected List<BaseElement> getAllElements(org.openqa.selenium.By locator, String elementName) {
        LOG.debug("🔍 Fetching list of elements for: {}", elementName);
        WaitHelper.waitForVisible(locator);
        List<BaseElement> elements = driver.findElements(locator)
                .stream()
                .map(e -> BaseElement.el(e, elementName))
                .collect(Collectors.toList());

        if (elements.isEmpty()) {
            LOG.error("❌ No elements found for: {}", elementName);
            throw new IllegalStateException("No elements found for: " + elementName);
        }

        LOG.debug("✅ Found {} element(s) for: {}", elements.size(), elementName);
        return elements;
    }

    // ===================== INPUT HANDLER =====================

    @Step("Type '{value}' into {fieldName}")
    protected void typeText(BaseElement element, String value, String fieldName) {
        element.shouldBe(Condition.VISIBLE);
        element.scrollTo();
        element.clearAndSetText(value);
        LOG.debug("⌨️ Typed '{}' into {}", value, fieldName);
    }

}
