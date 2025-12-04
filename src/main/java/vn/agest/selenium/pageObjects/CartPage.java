package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.LogHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(CartPage.class);

    // ======================= LOCATORS =======================
    private final BaseElement cartTable = $css(".table-responsive table.shop_table", "Cart Table");
    private final BaseElement cartItemRows = $css("tr.cart_item", "Cart Item Row");
    private final BaseElement cartItemName = $css("a.product-title", "Cart Item Name");
    private final BaseElement cartItemPrice = $css(".product-price .amount", "Cart Item Price");
    private final BaseElement cartItemQty = $css(".product-quantity input.qty", "Cart Item Quantity");
    private final BaseElement checkoutButton = $css("a.checkout-button", "Proceed to Checkout Button");

    // ======================= CONSTRUCTOR =======================
    public CartPage() {
        super(PageType.CART_PAGE);
    }

    // ======================= CORE ACTIONS =======================
    @Step("Get all products currently displayed in the Shopping Cart page")
    public List<Product> getCartProductInfo() {
        LOG.info("🛒 Getting product info list from Shopping Cart page...");
        WaitHelper.waitForVisible(cartTable.getLocator());
        List<BaseElement> cartRows = getAllCartRows();
        List<Product> products = extractCartProducts(cartRows);
        LogHelper.logProductList("🛒 Extracted cart products", products);
        LOG.info("✅ Found {} product(s) in cart.", products.size());
        return products;
    }

    @Step("Navigate to Checkout page")
    public CheckoutPage navigateToCheckoutPage() {
        LOG.info("🔜 Navigating to Checkout page...");
        checkoutButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        checkoutButton.scrollTo().click();
        LOG.debug("✅ Navigated to Checkout page.");
        return new CheckoutPage();
    }

    // ======================= SUPPORT METHODS =======================
    @Step("Get all cart row elements")
    private List<BaseElement> getAllCartRows() {
        return getAllElements(cartItemRows.getLocator(), "Cart Row");
    }

    @Step("Extract product info from each cart row")
    private List<Product> extractCartProducts(List<BaseElement> rows) {
        List<Product> products = new ArrayList<>();

        for (BaseElement row : rows) {
            String name = row.findChild(cartItemName.getLocator()).getText().trim();
            String priceText = row.findChild(cartItemPrice.getLocator()).getText().trim();
            double price = DataHelper.parsePrice(priceText);

            String qtyText = row.findChild(cartItemQty.getLocator()).getAttribute("value").trim();
            int quantity = DataHelper.parseQuantity(qtyText);

            products.add(new Product(name, price, quantity));
            LOG.debug("🧾 Cart item: {} | Price: {} | Qty: {}", name, price, quantity);
        }

        return products;
    }

    // ======================= SCREENSHOT SUPPORT =======================
    public WebElement getCartTableElement() {
        return cartTable.getWebElement();
    }
}
