package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.model.BillingInfo;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(OrderStatusPage.class);

    private static final String EXPECTED_URL_FRAGMENT = "/checkout/order-received/";

    private final BaseElement orderSuccessMessage =
            $css(".woocommerce-thankyou-order-received", "Order Success Message");
    private final BaseElement orderSection =
            $css(".woocommerce-order", "Order Section");
    private final BaseElement orderDetailsTable =
            $css(".woocommerce-table--order-details", "Order Details Table");
    private final BaseElement orderItemRow =
            $css("tr.woocommerce-table__line-item", "Order Item Row");
    private final BaseElement orderItemName =
            $css(".woocommerce-table__product-name a", "Order Item Name");
    private final BaseElement orderItemPrice =
            $css(".woocommerce-Price-amount bdi", "Order Item Price");
    private final BaseElement billingInfoSection =
            $css(".woocommerce-customer-details address", "Billing Info Section");
    private final BaseElement paymentMethod =
            $css(".woocommerce-order-overview__payment-method strong", "Payment Method");

    // ===================== CONSTRUCTOR =====================
    public OrderStatusPage() {
        super(null);
    }

    // ===================== VERIFY PAGE =====================
    @Step("Verify Order Received section loaded successfully")
    public boolean isLoaded() {
        LOG.info("🔍 Verifying Order Received section...");
        try {
            WaitHelper.waitForUrlContains(EXPECTED_URL_FRAGMENT);
            orderSection.shouldBe(Condition.VISIBLE);
            LOG.info("✅ Order Received verified successfully.");
            return true;
        } catch (Exception e) {
            LOG.error("❌ Order Status Page failed to load properly: {}", e.getMessage());
            return false;
        }
    }

    @Step("Get order confirmation message")
    public String getOrderConfirmationMessage() {
        try {
            String message = orderSuccessMessage.getText().trim();
            LOG.info("✅ Order confirmation message retrieved: {}", message);
            return message;
        } catch (Exception e) {
            LOG.error("❌ Failed to retrieve order confirmation message: {}", e.getMessage());
            return "";
        }
    }

    // ===================== PRODUCT INFO =====================
    @Step("Extract product details from Order Status page")
    public List<Product> getOrderProductInfo() {
        LOG.info("📦 Extracting product details from Order Status page...");
        WaitHelper.waitForVisible(orderDetailsTable.getLocator());

        List<BaseElement> rows = getAllElements(orderItemRow.getLocator(), "Order Item Row");
        List<Product> products = new ArrayList<>();

        for (BaseElement row : rows) {
            products.add(parseProductRow(row));
        }

        LogHelper.logProductList("✅ Extracted ordered products", products);
        LOG.info("✅ Found {} product(s) in Order Received page.", products.size());
        return products;
    }

    @Step("Parse single product row from Order Received table")
    private Product parseProductRow(BaseElement row) {
        String name = row.findChild(orderItemName.getLocator()).getText().trim();
        String priceText = row.findChild(orderItemPrice.getLocator()).getText().trim();

        BigDecimal totalPrice = ParseHelper.parsePriceToBigDecimal(priceText);
        int qty = ParseHelper.parseQuantityFromText(row.getText());
        if (qty <= 0) qty = 1;

        BigDecimal unitPrice = ProductUtils.calculateUnitPrice(totalPrice, qty);
        return new Product(name, unitPrice.doubleValue(), qty);
    }

    // ===================== BILLING INFO =====================
    @Step("Extract Billing Info from Order Status page")
    public BillingInfo getDisplayedBillingInfo() {
        LOG.info("🧾 Extracting Billing Info from Order Status page...");

        String rawText = billingInfoSection.getText();
        String normalized = ParseHelper.normalizeBillingText(rawText);
        List<String> lines = ParseHelper.cleanTextLines(normalized);

        BillingInfo info = parseBillingInfoFromLines(lines);
        info.setPaymentMethod(getPaymentMethod());

        LOG.info("💳 Final Billing Info (with Payment Method): {}", info);
        return info;
    }

    private BillingInfo parseBillingInfoFromLines(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            LOG.warn("⚠️ Billing info text is empty or null.");
            return BillingInfo.builder().build();
        }

        String[] arr = lines.toArray(new String[0]);

        String fullName = ParseHelper.safeLine(arr, 0);
        String company = ParseHelper.safeLine(arr, 1);
        String street = ParseHelper.safeLine(arr, 2);
        String cityLine = ParseHelper.safeLine(arr, 3);
        String phone = ParseHelper.safeLine(arr, 4);
        String email = ParseHelper.safeLine(arr, 5);

        var nameParts = ParseHelper.splitName(fullName);
        var cityPostParts = ParseHelper.splitCityLine(cityLine);

        String firstName = nameParts[0];
        String lastName = nameParts[1];
        String city = cityPostParts[0];
        String state = cityPostParts[1];
        String postcode = cityPostParts[2];
        String country = CountryMapper.resolveCountry(state);

        BillingInfo info = BillingInfo.ofTrimmed(
                firstName,
                lastName,
                street,
                city,
                country,
                postcode,
                phone,
                email,
                ""
        );
        info.setCompany(company);

        LOG.debug("✅ Parsed BillingInfo from order text: {}", info);
        return info;
    }

    @Step("Get payment method from Order Status page")
    private String getPaymentMethod() {
        try {
            String method = paymentMethod.getText().trim();
            LOG.debug("💳 Extracted payment method: {}", method);
            return method;
        } catch (Exception e) {
            LOG.warn("⚠️ Payment method not found: {}", e.getMessage());
            return "";
        }
    }

    // ===================== SCREENSHOT SUPPORT =====================
    public WebElement getOrderElement() {
        return orderSuccessMessage.getWebElement();
    }

    public WebElement getOrderDetailsElement() {
        return orderDetailsTable.getWebElement();
    }

    public WebElement getBillingAddressElement() {
        return billingInfoSection.getWebElement();
    }
}
