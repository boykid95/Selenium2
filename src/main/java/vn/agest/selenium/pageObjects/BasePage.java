package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.agest.selenium.core.config.PageTitleLoader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.enums.ProductCategory;
import vn.agest.selenium.model.PageInfo;
import vn.agest.selenium.pageObjects.components.DepartmentMenuComponent;

import java.time.Duration;

public abstract class BasePage {

    private static final Logger LOG = LoggerManager.getLogger(BasePage.class);

    protected final WebDriver driver;
    protected final PageType pageType;
    protected final PageInfo pageInfo;

    private final DepartmentMenuComponent departmentMenu = new DepartmentMenuComponent();

    private final BaseElement popupCloseButton = new BaseElement(By.cssSelector("button.pum-close:nth-child(3)"), "Popup Close Button");


    public BasePage(PageType pageType) {
        this.driver = DriverManager.getDriver();
        this.pageType = pageType;
        if (pageType == null || pageType == PageType.PRODUCT_CATEGORY_PAGE) {
            this.pageInfo = null;
        } else {
            this.pageInfo = PageTitleLoader.get(pageType);
        }
    }

    // ===================== OPEN PAGE =====================

    @Step("Open page: {this.pageType}")
    public void open() {
        LOG.info("Opening [{}] → {}", pageType.name(), pageInfo.url());
        driver.get(pageInfo.url());
    }

    @Step("Navigate to page: {pageType}")
    public void navigateToPage(PageType pageType) {
        PageInfo pageInfo = PageTitleLoader.get(pageType);
        LOG.info("Navigating to [{}] → {}", pageType.name(), pageInfo.url());
        driver.get(pageInfo.url());
    }

    @Step("Navigate to category page: {category.displayName}")
    public void navigateToCategoryPage(ProductCategory category) {
        String fullUrl = PageTitleLoader.get(PageType.PRODUCT_CATEGORY_PAGE).url() + category.getUrlPath();
        LOG.info("🌐 Navigating to Category: {} → {}", category.getDisplayName(), fullUrl);
        driver.get(fullUrl);
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

    // ===================== COMMON POPUP HANDLER =====================

    @Step("Close popup if present")
    public void closePopupIfPresent() {
        LOG.info("Checking popup visibility...");

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

            WebElement popup = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(popupCloseButton.getLocator())
            );
            if (popup != null) {
                popupCloseButton.click();
                LOG.debug("✅ Popup closed successfully.");
            }
        } catch (TimeoutException e) {
            LOG.debug("No popup appeared within 3 seconds, continue test flow.");
        } catch (Exception e) {
            LOG.debug("Popup handling skipped: {}", e.getMessage());
        }
    }

}
