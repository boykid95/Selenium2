package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.List;

public class ProductCategoryPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(ProductCategoryPage.class);

    private static final By PRODUCT_ITEMS = By.xpath("//div[contains(@class,'ajax-content')]//div[contains(@class,'content-product')]");
    private static final By PRODUCT_TITLE = By.xpath(".//h2[contains(@class,'product-title')]/a");
    private static final By PRODUCT_PRICE = By.xpath(".//span[contains(@class,'price')]//span[contains(@class,'amount')][last()]");
    private static final By ADD_TO_CART_BUTTON = By.xpath(".//a[contains(@class,'add_to_cart_button')]");
    private static final By LOADER = By.xpath("//div[@class='et-loader']");

    private Product selectedProduct;

    public ProductCategoryPage() {
        super(PageType.PRODUCT_CATEGORY_PAGE);
    }

    // ======================= CORE ACTIONS =======================

    @Step("Add {count} random product(s) to cart")
    public void addRandomProductToCart(int count) {
        for (int i = 0; i < count; i++) {
            selectRandomProduct();
            clickAddToCartButton();
        }
    }

    @Step("Select random product from list")
    private void selectRandomProduct() {
        LOG.info("Selecting a random product from category page...");
        List<WebElement> products = driver.findElements(PRODUCT_ITEMS);

        if (products.isEmpty()) {
            throw new IllegalStateException("❌ No products found on category page!");
        }

        int randomIndex = DataHelper.getRandomNumber(0, products.size() - 1);
        WebElement randomProduct = products.get(randomIndex);

        String name = randomProduct.findElement(PRODUCT_TITLE).getText().trim();
        String priceText = randomProduct.findElement(PRODUCT_PRICE).getText().trim();
        double price = DataHelper.parsePrice(priceText);

        selectedProduct = new Product(name, price, 1);
        LOG.info("🛒 Selected product: {} | Price: {}", name, price);
    }


    @Step("Click 'Add to Cart' button for selected product")
    private void clickAddToCartButton() {
        LOG.info("Clicking 'Add to Cart' button for '{}'", selectedProduct.getName());
        BaseElement addToCartButton = new BaseElement(ADD_TO_CART_BUTTON, "Add To Cart Button");
        addToCartButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        addToCartButton.click();
        waitForLoaderToDisappear(selectedProduct);
    }

    @Step("Wait for Add to Cart loader to disappear for {product.name}")
    private void waitForLoaderToDisappear(Product product) {
        try {
            LOG.debug("⏳ Waiting for loader (//div[@class='et-loader']) for '{}'", product.getName());
            WaitHelper.waitForPresence(LOADER);
            WaitHelper.waitForInvisible(LOADER);
            LOG.debug("✅ Loader completed for '{}'", product.getName());
        } catch (Exception e) {
            LOG.debug("⚡ Loader not detected or already disappeared for '{}'", product.getName());
        }
    }

    // ======================= GETTERS =======================

    public Product getSelectedProduct() {
        return selectedProduct;
    }

}
