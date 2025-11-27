package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.BillingProfile;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.BillingInfo;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.LocatorHelper;
import vn.agest.selenium.utils.LogHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

import static vn.agest.selenium.elements.BaseElement.el;

public class CheckoutPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(CheckoutPage.class);

    // ===================== LOCATORS =====================
    private static final By ORDER_SUMMARY_TABLE = By.cssSelector(".shop_table.woocommerce-checkout-review-order-table");
    private static final By ORDER_ITEM_ROWS = By.cssSelector("tr.cart_item");
    private static final By ORDER_ITEM_NAME = By.cssSelector("td.product-name");
    private static final By ORDER_ITEM_QTY = By.cssSelector("td.product-name strong.product-quantity");
    private static final By ORDER_ITEM_PRICE = By.cssSelector("td.product-total span.woocommerce-Price-amount bdi");

    private static final By ORDER_REVIEW_OVERLAY = By.cssSelector("#order_review .blockUI.blockOverlay");
    private static final By PAYMENT_OVERLAY = By.cssSelector("#payment .blockUI.blockOverlay");

    private static final By FIRST_NAME_INPUT = By.cssSelector("#billing_first_name");
    private static final By LAST_NAME_INPUT = By.cssSelector("#billing_last_name");
    private static final By STREET_INPUT = By.cssSelector("input#billing_address_1");
    private static final By CITY_INPUT = By.cssSelector("input#billing_city");
    private static final By POSTCODE_INPUT = By.cssSelector("input#billing_postcode");
    private static final By PHONE_INPUT = By.cssSelector("#billing_phone");
    private static final By EMAIL_INPUT = By.cssSelector("input#billing_email");

    private static final By COUNTRY_DROPDOWN = By.id("select2-billing_country-container");
    private static final By COUNTRY_SEARCH_BOX = By.cssSelector("input.select2-search__field");
    private static final By COUNTRY_RESULTS = By.cssSelector("ul.select2-results__options");
    private static final String COUNTRY_OPTION_XPATH =
            "//li[contains(@class,'select2-results__option') and normalize-space()='%s']";

    private static final By PAYMENT_METHOD_BANK_RADIO = By.cssSelector("#payment_method_bacs");
    private static final By PAYMENT_METHOD_COD_RADIO = By.cssSelector("#payment_method_cod");

    // ===================== CONSTRUCTOR =====================
    public CheckoutPage() {
        super(PageType.CHECKOUT_PAGE);
    }

    // ===================== PAGE VERIFY =====================
    @Step("Verify Checkout page is loaded")
    public boolean isLoaded() {
        String expectedTitle = getExpectedTitle();
        String actualTitle = getPageTitle();
        boolean correct = actualTitle.contains(expectedTitle);
        if (correct)
            LOG.info("✅ Checkout page loaded successfully.");
        else
            LOG.error("❌ Title mismatch: expected '{}' but got '{}'", expectedTitle, actualTitle);
        return correct;
    }

    // ===================== OVERLAYS =====================
    @Step("Wait for Checkout overlays to disappear")
    private void waitForCheckoutOverlaysToDisappear() {
        LOG.info("⏳ Waiting for checkout overlays to disappear...");
        try {
            WaitHelper.waitForInvisible(ORDER_REVIEW_OVERLAY, 10);
            WaitHelper.waitForInvisible(PAYMENT_OVERLAY, 10);
            LOG.debug("✅ Overlays disappeared — checkout is stable.");
        } catch (Exception e) {
            LOG.warn("⚠️ Timeout or skipped overlay wait: {}", e.getMessage());
        }
    }

    // ===================== ORDER SUMMARY =====================
    @Step("Get all products listed in Checkout page order summary")
    public List<Product> getCheckoutProductInfo() {
        LOG.info("🔍 Extracting product details from Checkout page...");
        waitForCheckoutOverlaysToDisappear();
        WaitHelper.waitForVisible(ORDER_SUMMARY_TABLE);

        List<BaseElement> rows = getAllElements(ORDER_ITEM_ROWS, "Checkout Item Row");
        List<Product> products = new ArrayList<>();
        for (BaseElement row : rows) {
            String name = row.findChild(ORDER_ITEM_NAME).getText().replaceAll("×.*", "").trim();
            int qty = DataHelper.parseQuantity(row.findChild(ORDER_ITEM_QTY).getText().replace("×", "").trim());
            double totalPrice = DataHelper.parsePrice(row.findChild(ORDER_ITEM_PRICE).getText().trim());
            double unitPrice = totalPrice / qty;
            products.add(new Product(name, unitPrice, qty));
        }

        LogHelper.logProductList("📦 Extracted checkout products", products);
        LOG.info("✅ Found {} product(s) on Checkout page.", products.size());
        return products;
    }

    // ===================== BILLING INFO =====================
    @Step("Fill Billing Details with Default Profile and Payment Method")
    public BillingInfo fillBillingDetailsDefault() {
        BillingInfo expected = BillingProfile.DEFAULT.getInfo();
        fillBillingDetails(expected);
        selectPaymentMethod(expected.getPaymentMethod());
        BillingInfo captured = captureBillingInfo();
        LOG.info("🧾 Captured Billing Info after filling: {}", captured);
        return captured.normalize();
    }

    @Step("Fill Billing Details form")
    public void fillBillingDetails(BillingInfo billing) {
        LOG.info("🧾 Filling Billing Details for: {}", billing.getFullName());
        billing.normalize();

        selectCountry(billing.getCountry());
        el(FIRST_NAME_INPUT, "First Name").scrollTo().clearAndSetText(billing.getFirstName());
        el(LAST_NAME_INPUT, "Last Name").scrollTo().clearAndSetText(billing.getLastName());
        el(STREET_INPUT, "Street").scrollTo().clearAndSetText(billing.getStreet());
        el(CITY_INPUT, "City").scrollTo().clearAndSetText(billing.getCity());
        el(POSTCODE_INPUT, "Postcode").scrollTo().clearAndSetText(billing.getPostcode());
        el(PHONE_INPUT, "Phone").scrollTo().clearAndSetText(billing.getPhone());
        el(EMAIL_INPUT, "Email").scrollTo().clearAndSetText(billing.getEmail());

        LOG.debug("✅ Billing form filled successfully for {}", billing.getFullName());
    }

    @Step("Capture currently filled Billing Info from Checkout form")
    public BillingInfo captureBillingInfo() {
        String firstName = el(FIRST_NAME_INPUT, "First Name").getValue();
        String lastName = el(LAST_NAME_INPUT, "Last Name").getValue();
        String street = el(STREET_INPUT, "Street").getValue();
        String city = el(CITY_INPUT, "City").getValue();
        String postcode = el(POSTCODE_INPUT, "Postcode").getValue();
        String phone = el(PHONE_INPUT, "Phone").getValue();
        String email = el(EMAIL_INPUT, "Email").getValue();
        String country = el(COUNTRY_DROPDOWN, "Country Dropdown").getText();
        String payment = getSelectedPaymentMethod();

        return BillingInfo.ofTrimmed(firstName, lastName, street, city, postcode, country, phone, email, payment);
    }

    // ===================== COUNTRY SELECT =====================
    @Step("Select country: {countryName}")
    private void selectCountry(String countryName) {
        LOG.info("🌍 Selecting country: {}", countryName);
        BaseElement dropdown = el(COUNTRY_DROPDOWN, "Country dropdown");
        dropdown.scrollTo().click();

        WaitHelper.shortVisible(COUNTRY_SEARCH_BOX);
        el(COUNTRY_SEARCH_BOX, "Country search input").setText(countryName);
        WaitHelper.shortVisible(COUNTRY_RESULTS);

        clickCountryOption(countryName);
        LOG.debug("✅ Country selected successfully: {}", countryName);
    }

    @Step("Click country option: {countryName}")
    private void clickCountryOption(String countryName) {
        BaseElement option = LocatorHelper.getDynamicLocator(COUNTRY_OPTION_XPATH, countryName);

        try {
            option.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
            option.click();
        } catch (Exception e) {
            LOG.warn("⚠️ First click failed for '{}', retrying with JS click...", countryName);
            try {
                option.jsClick();
            } catch (Exception ex) {
                LOG.error("❌ Unable to click country option '{}': {}", countryName, ex.getMessage());
                throw ex;
            }
        }
    }

    // ===================== PAYMENT METHOD =====================
    @Step("Select payment method: {method}")
    private void selectPaymentMethod(String method) {
        String lower = method.toLowerCase();

        if (lower.contains("bank")) {
            el(PAYMENT_METHOD_BANK_RADIO, "Direct Bank Transfer Option").click();
            LOG.debug("🏦 Selected 'Direct Bank Transfer'");
        } else if (lower.contains("cod") || lower.contains("delivery")) {
            el(PAYMENT_METHOD_COD_RADIO, "Cash on Delivery Option").click();
            LOG.debug("💵 Selected 'Cash on Delivery'");
        } else {
            LOG.error("❌ Unsupported payment method: {}", method);
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }

    private String getSelectedPaymentMethod() {
        if (el(PAYMENT_METHOD_BANK_RADIO, "Direct Bank Transfer Option").getWebElement().isSelected())
            return "Direct bank transfer";
        if (el(PAYMENT_METHOD_COD_RADIO, "Cash on Delivery Option").getWebElement().isSelected())
            return "Cash on delivery";
        return "";
    }
}
