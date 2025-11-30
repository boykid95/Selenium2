package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.BillingProfile;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.BillingInfo;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.LogHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

public class CheckoutPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(CheckoutPage.class);
    private static final String COUNTRY_OPTION_XPATH =
            "//li[contains(@class,'select2-results__option') and normalize-space()='%s']";
    // ===================== LOCATORS =====================
    private final BaseElement checkoutHeaderText =
            $x("//a[contains(@class,'active') and normalize-space(text())='Checkout']", "Checkout Header Text");
    private final BaseElement orderSummaryTable =
            $css(".shop_table.woocommerce-checkout-review-order-table", "Order Summary Table");
    private final BaseElement orderItemRows =
            $css("tr.cart_item", "Order Item Row");
    private final BaseElement orderItemName =
            $css("td.product-name", "Order Item Name");
    private final BaseElement orderItemQty =
            $css("td.product-name strong.product-quantity", "Order Item Quantity");
    private final BaseElement orderItemPrice =
            $css("td.product-total span.woocommerce-Price-amount bdi", "Order Item Price");
    private final BaseElement orderReviewOverlay =
            $css("#order_review .blockUI.blockOverlay", "Order Review Overlay");
    private final BaseElement paymentOverlay =
            $css("#payment .blockUI.blockOverlay", "Payment Overlay");
    // Billing fields
    private final BaseElement firstNameInput = $css("#billing_first_name", "First Name");
    private final BaseElement lastNameInput = $css("#billing_last_name", "Last Name");
    private final BaseElement streetInput = $css("input#billing_address_1", "Street");
    private final BaseElement cityInput = $css("input#billing_city", "City");
    private final BaseElement postcodeInput = $css("input#billing_postcode", "Postcode");
    private final BaseElement phoneInput = $css("#billing_phone", "Phone");
    private final BaseElement emailInput = $css("input#billing_email", "Email");
    // Country dropdown
    private final BaseElement countryDropdown = $id("select2-billing_country-container", "Country Dropdown");
    private final BaseElement countrySearchBox = $css("input.select2-search__field", "Country Search Box");
    private final BaseElement countryResults = $css("ul.select2-results__options", "Country Results");
    // Payment methods
    private final BaseElement paymentMethodBankRadio = $css("#payment_method_bacs", "Direct Bank Transfer Option");
    private final BaseElement paymentMethodCodRadio = $css("#payment_method_cod", "Cash on Delivery Option");
    // Buttons & overlays
    private final BaseElement placeOrderButton = $css("#place_order", "Place Order Button");
    private final BaseElement pageOverlay = $css("div.blockUI.blockOverlay", "Page Overlay");

    // ===================== CONSTRUCTOR =====================
    public CheckoutPage() {
        super(PageType.CHECKOUT_PAGE);
    }

    // ===================== VERIFY PAGE =====================
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

    // ===================== OVERLAY CONTROL =====================
    @Step("Wait for Checkout overlays to disappear")
    private void waitForCheckoutOverlaysToDisappear() {
        LOG.info("⏳ Waiting for checkout overlays to disappear...");
        try {
            WaitHelper.waitForInvisible(orderReviewOverlay.getLocator(), 10);
            WaitHelper.waitForInvisible(paymentOverlay.getLocator(), 10);
            LOG.debug("✅ Overlays disappeared — checkout is stable.");
        } catch (Exception e) {
            LOG.warn("⚠️ Timeout or skipped overlay wait: {}", e.getMessage());
        }
    }

    // ===================== PRODUCT SUMMARY =====================
    @Step("Get all products listed in Checkout page order summary")
    public List<Product> getCheckoutProductInfo() {
        LOG.info("🔍 Extracting product details from Checkout page...");
        waitForCheckoutOverlaysToDisappear();
        WaitHelper.waitForVisible(orderSummaryTable.getLocator());

        List<BaseElement> rows = getAllElements(orderItemRows.getLocator(), "Checkout Item Row");
        List<Product> products = new ArrayList<>();

        for (BaseElement row : rows) {
            String name = row.findChild(orderItemName.getLocator()).getText().replaceAll("×.*", "").trim();
            int qty = DataHelper.parseQuantity(row.findChild(orderItemQty.getLocator()).getText().replace("×", "").trim());
            double totalPrice = DataHelper.parsePrice(row.findChild(orderItemPrice.getLocator()).getText().trim());
            double unitPrice = totalPrice / qty;
            products.add(new Product(name, unitPrice, qty));
        }

        LogHelper.logProductList("📦 Extracted checkout products", products);
        LOG.info("✅ Found {} product(s) on Checkout page.", products.size());
        return products;
    }

    // ===================== BILLING INFO =====================
    @Step("Fill Billing Details with Default Profile and return info")
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

        typeText(firstNameInput, billing.getFirstName(), "First Name");
        typeText(lastNameInput, billing.getLastName(), "Last Name");
        typeText(streetInput, billing.getStreet(), "Street");
        typeText(cityInput, billing.getCity(), "City");
        typeText(postcodeInput, billing.getPostcode(), "Postcode");
        typeText(phoneInput, billing.getPhone(), "Phone");
        typeText(emailInput, billing.getEmail(), "Email");

        LOG.debug("✅ Billing form filled successfully for {}", billing.getFullName());
    }

    @Step("Capture currently filled Billing Info from Checkout form")
    public BillingInfo captureBillingInfo() {
        return BillingInfo.ofTrimmed(
                firstNameInput.getValue(),
                lastNameInput.getValue(),
                streetInput.getValue(),
                cityInput.getValue(),
                postcodeInput.getValue(),
                countryDropdown.getText(),
                phoneInput.getValue(),
                emailInput.getValue(),
                getSelectedPaymentMethod()
        );
    }

    // ===================== COUNTRY =====================
    @Step("Select country: {countryName}")
    private void selectCountry(String countryName) {
        LOG.info("🌍 Selecting country: {}", countryName);

        countryDropdown.scrollTo().click();
        WaitHelper.shortVisible(countrySearchBox.getLocator());
        countrySearchBox.setText(countryName);
        WaitHelper.shortVisible(countryResults.getLocator());

        clickCountryOption(countryName);
        LOG.debug("✅ Country selected successfully: {}", countryName);
    }

    @Step("Click country option: {countryName}")
    private void clickCountryOption(String countryName) {
        BaseElement option = getDynamicElement(COUNTRY_OPTION_XPATH, countryName);
        option.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        option.click();
        LOG.debug("✅ Country option '{}' clicked successfully.", countryName);
    }

    // ===================== PAYMENT =====================
    @Step("Select payment method: {method}")
    private void selectPaymentMethod(String method) {
        String lower = method.toLowerCase();

        if (lower.contains("bank")) {
            paymentMethodBankRadio.selectIfNotSelected();
            LOG.debug("🏦 Selected 'Direct Bank Transfer'");
        } else if (lower.contains("cod") || lower.contains("delivery")) {
            paymentMethodCodRadio.selectIfNotSelected();
            LOG.debug("💵 Selected 'Cash on Delivery'");
        } else {
            LOG.error("❌ Unsupported payment method: {}", method);
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }

    private String getSelectedPaymentMethod() {
        if (paymentMethodBankRadio.getWebElement().isSelected())
            return "Direct bank transfer";
        if (paymentMethodCodRadio.getWebElement().isSelected())
            return "Cash on delivery";
        return "";
    }

    // ===================== ORDER FLOW =====================
    @Step("Click 'Place Order' button")
    public void clickPlaceOrderButton() {
        LOG.info("🖱 Clicking 'Place Order' button...");
        placeOrderButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        placeOrderButton.scrollTo().click();
        LOG.debug("✅ 'Place Order' button clicked.");
    }

    @Step("Wait for WooCommerce order processing overlay to disappear")
    public void waitForProcessingOverlayToDisappear() {
        LOG.debug("⏳ Waiting for WooCommerce overlay ...");
        WaitHelper.waitForAppearAndDisappear(pageOverlay.getLocator());
        LOG.info("✅ Overlay disappeared and page fully loaded.");
    }

    @Step("Place order and navigate to Order Status Page")
    public void navigateToOrderStatusPage() {
        LOG.info("🚀 Initiating 'Place Order' sequence...");
        clickPlaceOrderButton();
        waitForProcessingOverlayToDisappear();
        LOG.info("✅ Checkout process completed. Navigating to Order Status Page...");
    }

    // ===================== SCREENSHOT SUPPORT =====================
    public WebElement getCheckoutHeaderElement() {
        return checkoutHeaderText.getWebElement();
    }

    public WebElement getOrderSummaryTableElement() {
        return orderSummaryTable.getWebElement();
    }
}
