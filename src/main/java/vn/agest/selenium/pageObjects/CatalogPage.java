package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CatalogPage extends BasePage {

    // ================= LOCATORS =================
    protected static final By PRODUCT_ITEMS =
            By.xpath("//div[contains(@class,'ajax-content')]//div[contains(@class,'content-product')]");
    protected static final By PRODUCT_TITLE =
            By.xpath(".//h2[contains(@class,'product-title')]/a");
    protected static final By PRODUCT_PRICE =
            By.xpath(".//span[contains(@class,'price')]");
    protected static final By ADD_TO_CART_BUTTON =
            By.xpath(".//div[contains(@class,'product-details')]//a[contains(@class,'add_to_cart_button')]");
    protected static final By LOADER =
            By.xpath("//div[@class='et-loader']");
    protected static final By VIEW_CART_BUTTON =
            By.xpath("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]");
    protected static final By DISCOUNTED_PRICE = By.cssSelector(".products .price ins .amount");  // Giá giảm
    protected static final By NORMAL_PRICE = By.cssSelector(".products .price .amount");

    private static final Logger LOG = LoggerManager.getLogger(CatalogPage.class);
    protected BaseElement selectedProductElement;
    protected Product selectedProduct;

    protected CatalogPage(PageType pageType) {
        super(pageType);
    }

    // ================= COMMON PRODUCT ACTIONS =================

    @Step("Get all available products from catalog")
    protected List<BaseElement> getAllProducts() {
        LOG.debug("🔍 Fetching product list...");
        WaitHelper.waitForVisible(PRODUCT_ITEMS);

        List<BaseElement> products = driver.findElements(PRODUCT_ITEMS)
                .stream()
                .map(e -> BaseElement.el(e, "Product Item"))
                .collect(Collectors.toList());

        if (products.isEmpty()) {
            LOG.error("❌ No products found on catalog page!");
            throw new IllegalStateException("No products found!");
        }

        LOG.debug("✅ Found {} product(s) in catalog.", products.size());
        return products;
    }

    @Step("Select product at index {index}")
    protected void selectProductByIndex(List<BaseElement> products, int index) {
        selectedProductElement = products.get(index);

        String name = selectedProductElement.findChild(PRODUCT_TITLE).getText().trim();
        String priceText = extractPrice(selectedProductElement.findChild(PRODUCT_PRICE));
        double price = DataHelper.parsePrice(priceText);

        selectedProduct = new Product(name, price, 1);
        LOG.info("✅ Selected product [{}]: {} | Price: {}", index, name, price);
    }

    @Step("Extract visible price from product element")
    protected String extractPrice(BaseElement priceContainer) {
        List<BaseElement> priceElements = priceContainer.findBaseElements(DISCOUNTED_PRICE);
        if (!priceElements.isEmpty()) {
            return priceElements.get(0).getText().trim();
        }

        priceElements = priceContainer.findBaseElements(NORMAL_PRICE);
        if (!priceElements.isEmpty()) {
            return priceElements.get(priceElements.size() - 1).getText().trim();  // Giá bình thường
        }

        LOG.warn("⚠️ No price text found for product!");
        return "";
    }

    @Step("Click 'Add to Cart' for selected product")
    protected void clickAddToCartButton() {
        LOG.info("🛒 Clicking 'Add to Cart' for '{}'", selectedProduct.getName());
        BaseElement addToCart = selectedProductElement.findChild(ADD_TO_CART_BUTTON);
        addToCart.scrollTo();
        addToCart.click();
        waitForLoaderToDisappear();
    }

    @Step("Wait for loader to disappear after adding to cart")
    protected void waitForLoaderToDisappear() {
        try {
            WaitHelper.waitForPresence(LOADER);
            WaitHelper.waitForInvisible(LOADER);
            LOG.debug("✅ Loader disappeared successfully.");
        } catch (Exception e) {
            LOG.debug("⚡ Loader not found or already gone.");
        }
    }

    @Step("Add {count} random product(s) to cart")
    public List<Product> addRandomProductsToCart(int count) {
        LOG.info("🛍 Adding {} random product(s) to cart...", count);

        List<BaseElement> products = getAllProducts();
        List<Integer> indexes = DataHelper.getRandomIndexes(products.size(), count);

        List<Product> selectedProducts = new ArrayList<>();
        for (Integer i : indexes) {
            selectProductByIndex(products, i);
            clickAddToCartButton();
            selectedProducts.add(selectedProduct);
        }

        LOG.info("✅ Added {} product(s) to cart", selectedProducts.size());
        return selectedProducts;
    }

    @Step("Navigate to Shopping Cart page via View Cart button")
    public CartPage navigateToShoppingCartPage() {
        LOG.info("🛒 Navigating to Shopping Cart page...");
        BaseElement viewCartButton = BaseElement.el(VIEW_CART_BUTTON, "View Cart Button");
        viewCartButton.scrollTo();
        viewCartButton.click();
        LOG.debug("✅ Shopping Cart page opened.");
        return new CartPage();
    }

    // ================ Getter ================
    public Product getSelectedProduct() {
        return selectedProduct;
    }
}
