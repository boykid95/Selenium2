package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.List;

public class ShoppingCartPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(ShoppingCartPage.class);

    private static final By CART_TABLE = By.cssSelector(".table-responsive table.shop_table");
    private static final By CART_ITEM_ROWS = By.cssSelector("tr.cart_item");
    private static final By CART_ITEM_NAME = By.cssSelector("a.product-title");
    private static final By CART_ITEM_PRICE = By.cssSelector(".product-price .amount");
    private static final By CART_ITEM_QTY = By.cssSelector(".product-quantity input.qty");

    public ShoppingCartPage() {
        super(PageType.CART_PAGE);
    }

    @Step("Verify selected product from category page is displayed correctly in cart")
    public boolean verifyCartItemMatchesSelectedProduct(ProductCategoryPage categoryPage) {
        Product expected = categoryPage.getSelectedProduct();
        LOG.info("🧾 Verifying product in cart matches selected product: {}", expected.getName());

        WaitHelper.waitForVisible(CART_TABLE);

        List<WebElement> cartRows = driver.findElements(CART_ITEM_ROWS);
        if (cartRows.isEmpty()) {
            LOG.error("❌ No cart items found!");
            return false;
        }

        for (WebElement row : cartRows) {
            BaseElement rowElement = BaseElement.el(row, "Cart Item Row");

            String actualName = rowElement.findChild(CART_ITEM_NAME).getText().trim();
            String actualPriceText = rowElement.findChild(CART_ITEM_PRICE).getText().trim();
            double actualPrice = DataHelper.parsePrice(actualPriceText);

            WebElement qtyField = rowElement.findChild(CART_ITEM_QTY).getWebElement();
            int actualQuantity = Integer.parseInt(qtyField.getAttribute("value").trim());

            LOG.info("→ Found in cart: {} | ${} | Qty: {}", actualName, actualPrice, actualQuantity);

            if (actualName.equalsIgnoreCase(expected.getName())
                    && actualPrice == expected.getPrice()
                    && actualQuantity == expected.getQuantity()) {
                LOG.info("✅ Cart item matches expected product.");
                return true;
            }
        }

        LOG.error("❌ Expected product not found in cart!");
        return false;
    }
}
