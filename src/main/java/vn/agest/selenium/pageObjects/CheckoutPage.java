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
import vn.agest.selenium.utils.LogHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

import static vn.agest.selenium.elements.BaseElement.el;

public class CheckoutPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(CheckoutPage.class);

    // Locator definitions for order items
    private static final By ORDER_SUMMARY_TABLE = By.cssSelector(".shop_table.woocommerce-checkout-review-order-table");
    private static final By ORDER_ITEM_ROWS = By.cssSelector("tr.cart_item");
    private static final By ORDER_ITEM_NAME = By.cssSelector("td.product-name");
    private static final By ORDER_ITEM_QTY = By.cssSelector("td.product-name strong.product-quantity");
    private static final By ORDER_ITEM_PRICE = By.cssSelector("td.product-total span.woocommerce-Price-amount bdi");

    private static final By SUBTOTAL_PRICE = By.cssSelector("tr.cart-subtotal span.woocommerce-Price-amount bdi");
    private static final By TOTAL_PRICE = By.cssSelector("tr.order-total span.woocommerce-Price-amount bdi");

    // Locator for Place Order button and loading overlay
    private static final By PLACE_ORDER_BUTTON = By.cssSelector("#place_order");
    private static final By LOADING_OVERLAY = By.cssSelector(".blockUI.blockOverlay");

    // Billing info locators
    private static final By FIRST_NAME_INPUT = By.cssSelector("#billing_first_name");
    private static final By LAST_NAME_INPUT = By.cssSelector("#billing_last_name");
    private static final By STREET_INPUT = By.cssSelector("input#billing_address_1");
    private static final By CITY_INPUT = By.cssSelector("input#billing_city");
    private static final By POSTCODE_INPUT = By.cssSelector("input#billing_postcode");
    private static final By PHONE_INPUT = By.cssSelector("#billing_phone");
    private static final By EMAIL_INPUT = By.cssSelector("input#billing_email");
    private static final By COUNTRY_DROPDOWN = By.cssSelector("select2-billing_country-container");
    private static final By COUNTRY_SEARCH_BOX = By.cssSelector("input.select2-search__field");

    // Payment method locators
    private static final By PAYMENT_METHOD_BANK_RADIO = By.cssSelector("#payment_method_bacs");
    private static final By PAYMENT_METHOD_COD_RADIO = By.cssSelector("#payment_method_cod");

    public CheckoutPage() {
        super(PageType.CHECKOUT_PAGE);
    }

    // ===================== VERIFY PAGE LOADED =====================

    @Step("Verify Checkout page is loaded")
    public boolean isLoaded() {
        String expectedTitle = getExpectedTitle();
        String actualTitle = getPageTitle();

        LOG.debug("Expected title: {}", expectedTitle);
        LOG.debug("Actual title: {}", actualTitle);

        boolean isTitleCorrect = actualTitle.contains(expectedTitle);
        if (isTitleCorrect) {
            LOG.info("✅ Checkout page loaded successfully with correct title.");
        } else {
            LOG.error("❌ Checkout page title mismatch. Expected: '{}' but got: '{}'", expectedTitle, actualTitle);
        }

        return isTitleCorrect;
    }

    @Step("Get all products listed in Checkout page order summary")
    public List<Product> getCheckoutProductInfo() {
        LOG.info("🔍 Extracting product details from Checkout page...");

        WaitHelper.waitForVisible(ORDER_SUMMARY_TABLE);
        List<BaseElement> checkoutRows = getAllCheckoutRows();
        List<Product> products = extractCheckoutProducts(checkoutRows);

        LogHelper.logProductList("📦 Extracted checkout products", products);
        LOG.info("✅ Found {} product(s) on Checkout page.", products.size());
        return products;
    }

    @Step("Get all checkout item rows from order summary table")
    private List<BaseElement> getAllCheckoutRows() {
        return getAllElements(ORDER_ITEM_ROWS, "Checkout Item Row");
    }

    @Step("Extract product info from each checkout row")
    private List<Product> extractCheckoutProducts(List<BaseElement> rows) {
        List<Product> products = new ArrayList<>();

        for (BaseElement row : rows) {
            String name = extractProductName(row);
            int quantity = extractQuantity(row);
            double price = extractPrice(row);

            Product product = new Product(name, price, quantity);
            products.add(product);

            LOG.debug("🧾 Checkout item: '{}' | Qty: {} | Price: {}", name, quantity, price);
        }
        return products;
    }

    private String extractProductName(BaseElement row) {
        return row.findChild(ORDER_ITEM_NAME)
                .getText()
                .replaceAll("×.*", "")
                .trim();
    }

    private int extractQuantity(BaseElement row) {
        String qtyText = row.findChild(ORDER_ITEM_QTY)
                .getText()
                .replace("×", "")
                .trim();
        return DataHelper.parseQuantity(qtyText);
    }

    private double extractPrice(BaseElement row) {
        String priceText = row.findChild(ORDER_ITEM_PRICE)
                .getText()
                .trim();
        return DataHelper.parsePrice(priceText);
    }

    // ================= SUBTOTAL & TOTAL =================

    @Step("Get Subtotal price displayed on Checkout page")
    public double getSubtotal() {
        String subtotalText = el(SUBTOTAL_PRICE).getText().trim();
        double subtotal = DataHelper.parsePrice(subtotalText);
        LOG.info("💰 Subtotal: {}", subtotal);
        return subtotal;
    }

    @Step("Get Total price displayed on Checkout page")
    public double getTotal() {
        String totalText = el(TOTAL_PRICE).getText().trim();
        double total = DataHelper.parsePrice(totalText);
        LOG.info("💰 Total: {}", total);
        return total;
    }

    // ===================== FILL GUEST BILLING INFO =====================
    @Step("Fill Billing Details with Default Profile and Payment Method")
    public BillingInfo fillBillingDetailsDefault() {
        BillingInfo billing = BillingProfile.DEFAULT.getInfo();
        fillBillingDetails(billing);
        selectPaymentMethod(billing.getPaymentMethod());
        LOG.info("💳 Payment method selected: {}", billing.getPaymentMethod());
        return billing;
    }

    @Step("Fill Billing Details form")
    public void fillBillingDetails(BillingInfo billing) {
        LOG.info("🧾 Filling Billing Details for: {}", billing.getFullName());

        el(FIRST_NAME_INPUT, "First Name").setText(billing.getFirstName());
        el(LAST_NAME_INPUT, "Last Name").setText(billing.getLastName());
        el(STREET_INPUT, "Street Address").setText(billing.getStreet());
        el(CITY_INPUT, "City").setText(billing.getCity());
        selectCountry(billing.getCountry());
        el(POSTCODE_INPUT, "Postcode").setText(billing.getPostcode());
        el(PHONE_INPUT, "Phone").setText(billing.getPhone());
        el(EMAIL_INPUT, "Email").setText(billing.getEmail());

        LOG.debug("✅ Billing form filled successfully for {}", billing.getFullName());
    }

    @Step("Select country: {countryName}")
    private void selectCountry(String countryName) {
        LOG.info("🌍 Selecting country: {}", countryName);

        // Chờ dropdown hiển thị và click vào để mở
        el(COUNTRY_DROPDOWN, "Country dropdown").click();

        // Chờ các tùy chọn trong dropdown xuất hiện
        WaitHelper.waitForVisible(By.cssSelector("ul.select2-results__options"));

        // Nhập tên quốc gia vào ô tìm kiếm
        el(COUNTRY_SEARCH_BOX, "Country search input").setText(countryName);

        // Chờ quốc gia hiển thị trong dropdown
        $x(String.format("//li[contains(@class,'select2-results__option') and normalize-space()='%s']", countryName))
                .shouldBe(Condition.VISIBLE);

        // Chọn quốc gia từ danh sách
        $x(String.format("//li[contains(@class,'select2-results__option') and normalize-space()='%s']", countryName)).click();

        LOG.debug("✅ Country selected successfully: {}", countryName);
    }


    // ===================== SELECT PAYMENT METHOD =====================

    @Step("Select payment method: {method}")
    private void selectPaymentMethod(String method) {
        String methodLower = method.toLowerCase();

        if (methodLower.contains("bank")) {
            el(PAYMENT_METHOD_BANK_RADIO, "Direct Bank Transfer Option").click();
            LOG.debug("🏦 Selected 'Direct Bank Transfer'");
        } else if (methodLower.contains("cod") || methodLower.contains("delivery")) {
            el(PAYMENT_METHOD_COD_RADIO, "Cash on Delivery Option").click();
            LOG.debug("💵 Selected 'Cash on Delivery'");
        } else {
            LOG.error("❌ Unsupported payment method: {}", method);
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }

//    // ===================== VERIFY ORDER ITEM DETAILS =====================
//
//    @Step("Verify item details on Checkout page match selected product")
//    public boolean verifyOrderItemDetails(Product expectedProduct) {
//        String actualName = el(ORDER_ITEM_NAME).getText().trim();
//        double actualPrice = parsePrice(el(ORDER_ITEM_PRICE).getText());
//
//        LOG.info("Comparing Order Item: Expected Name: {} | Actual Name: {}", expectedProduct.getName(), actualName);
//        LOG.info("Expected Price: {} | Actual Price: {}", expectedProduct.getPrice(), actualPrice);
//
//        return actualName.toLowerCase().contains(expectedProduct.getName().toLowerCase()) &&
//                Math.abs(actualPrice - expectedProduct.getPrice()) < 0.01;
//    }
//
//    // ===================== PLACE ORDER =====================
//
//    @Step("Click on 'Place Order' button")
//    public OrderStatusPage placeOrder() {
//        el(PLACE_ORDER_BUTTON).click();
//        waitForLoadingOverlay();
//        return new OrderStatusPage();
//    }
//
//    // ===================== WAIT FOR LOADING OVERLAY =====================
//
//    @Step("Wait for loading overlay to appear and disappear")
//    private void waitForLoadingOverlay() {
//        try {
//            el(LOADING_OVERLAY).shouldBe(Condition.VISIBLE);
//        } catch (Exception e) {
//            LOG.info("Loading overlay did not appear immediately, continuing...");
//        }
//        el(LOADING_OVERLAY).shouldNotBe(Condition.visible);
//    }
//


//    // ===================== UTILITY METHOD =====================
//
//    private double parsePrice(String priceText) {
//        return Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
//    }
}
