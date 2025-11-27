package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.LogHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(CartPage.class);

    private static final By CART_TABLE = By.cssSelector(".table-responsive table.shop_table");
    private static final By CART_ITEM_ROWS = By.cssSelector("tr.cart_item");
    private static final By CART_ITEM_NAME = By.cssSelector("a.product-title");
    private static final By CART_ITEM_PRICE = By.cssSelector(".product-price .amount");
    private static final By CART_ITEM_QTY = By.cssSelector(".product-quantity input.qty");
    private static final By CHECKOUT_BUTTON = By.cssSelector("a.checkout-button");

    public CartPage() {
        super(PageType.CART_PAGE);
    }

    @Step("Get all products currently displayed in the Shopping Cart page")
    public List<Product> getCartProductInfo() {
        LOG.info("🛒 Getting product info list from Shopping Cart page...");

        WaitHelper.waitForVisible(CART_TABLE);
        List<BaseElement> cartRows = getAllCartRows();
        List<Product> products = extractCartProducts(cartRows);
        LogHelper.logProductList("🛒 Extracted cart products", products);

        LOG.info("✅ Found {} product(s) in cart.", products.size());
        return products;
    }

    @Step("Get all cart row elements")
    private List<BaseElement> getAllCartRows() {
        return getAllElements(CART_ITEM_ROWS, "Cart Row");
    }

    @Step("Extract product info from each cart row")
    private List<Product> extractCartProducts(List<BaseElement> rows) {
        List<Product> products = new ArrayList<>();
        for (BaseElement row : rows) {
            String name = row.findChild(CART_ITEM_NAME).getText().trim();
            String priceText = row.findChild(CART_ITEM_PRICE).getText().trim();
            double price = DataHelper.parsePrice(priceText);

            String qtyText = row.findChild(CART_ITEM_QTY).getAttribute("value").trim();
            int quantity = DataHelper.parseQuantity(qtyText);

            Product product = new Product(name, price, quantity);
            products.add(product);

            LOG.debug("🧾 Cart item: {} | Price: {} | Qty: {}", name, price, quantity);
        }
        return products;
    }

    @Step("Navigate to Checkout page")
    public CheckoutPage navigateToCheckoutPage() {
        LOG.info("🔜 Navigating to Checkout page...");

        BaseElement proceedToCheckoutButton = BaseElement.el(CHECKOUT_BUTTON, "Proceed to Checkout Button");
        proceedToCheckoutButton.scrollTo();
        proceedToCheckoutButton.click();

        return new CheckoutPage();
    }
}
