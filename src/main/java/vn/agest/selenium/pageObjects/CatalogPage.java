package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.utils.DataHelper;
import vn.agest.selenium.utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CatalogPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(CatalogPage.class);

    // ================= LOCATORS (DSL) =================
    protected final BaseElement productItems =
            $x("//div[contains(@class,'ajax-content')]//div[contains(@class,'content-product')]", "Product Items");
    protected final BaseElement productTitle =
            $x(".//h2[contains(@class,'product-title')]/a", "Product Title");
    protected final BaseElement productPrice =
            $x(".//span[contains(@class,'price')]", "Product Price");
    protected final BaseElement addToCartButton =
            $x(".//div[contains(@class,'product-details')]//a[contains(@class,'add_to_cart_button')]", "Add to Cart Button");
    protected final BaseElement loader =
            $x("//div[@class='et-loader']", "Loader Spinner");
    protected final BaseElement viewCartButton =
            $x("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]", "View Cart Button");
    protected final BaseElement discountedPrice =
            $css(".products .price ins .amount", "Discounted Price");
    protected final BaseElement normalPrice =
            $css(".products .price .amount", "Normal Price");

    // ================= STATE VARIABLES =================
    protected BaseElement selectedProductElement;
    protected Product selectedProduct;

    // ================= CONSTRUCTOR =================
    protected CatalogPage(PageType pageType) {
        super(pageType);
    }

    // ================= METHODS =================

    @Step("Get all available products from catalog")
    protected List<BaseElement> getAllProducts() {
        LOG.debug("🔍 Fetching product list...");
        WaitHelper.waitForVisible(productItems.getLocator());

        List<BaseElement> products = driver.findElements(productItems.getLocator())
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

        String name = selectedProductElement.findChild(productTitle.getLocator()).getText().trim();
        String priceText = extractPrice(selectedProductElement.findChild(productPrice.getLocator()));
        double price = DataHelper.parsePrice(priceText);

        selectedProduct = new Product(name, price, 1);
        LOG.info("✅ Selected product [{}]: {} | Price: {}", index, name, price);
    }

    @Step("Extract visible price from product element")
    protected String extractPrice(BaseElement priceContainer) {
        List<BaseElement> priceElements = priceContainer.findBaseElements(discountedPrice.getLocator());
        if (!priceElements.isEmpty()) {
            return priceElements.get(0).getText().trim();
        }

        priceElements = priceContainer.findBaseElements(normalPrice.getLocator());
        if (!priceElements.isEmpty()) {
            return priceElements.get(priceElements.size() - 1).getText().trim();
        }

        LOG.warn("⚠️ No price text found for product!");
        return "";
    }

    @Step("Click 'Add to Cart' for selected product")
    protected void clickAddToCartButton() {
        LOG.info("🛒 Clicking 'Add to Cart' for '{}'", selectedProduct.getName());

        BaseElement addButton = selectedProductElement.findChild(addToCartButton.getLocator());
        addButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        addButton.scrollTo().click();

        waitForLoaderToDisappear();
    }

    @Step("Wait for loader to appear and disappear")
    protected void waitForLoaderToDisappear() {
        LOG.debug("⏳ Waiting for loader to appear/disappear...");
        WaitHelper.waitForAppearAndDisappear(loader.getLocator());
    }

    @Step("Add {count} random product(s) to cart (duplicates allowed)")
    public List<Product> addRandomProductsToCart(int count) {
        LOG.info("🛒 Adding {} random product(s) to cart (duplicates allowed)...", count);

        WaitHelper.waitForVisible(productItems.getLocator());
        List<BaseElement> products = getAllProducts();
        int totalAvailable = products.size();

        if (totalAvailable == 0) {
            LOG.error("❌ No products found on catalog page!");
            throw new IllegalStateException("No products available to add to cart");
        }

        LOG.debug("✅ Found {} products → duplicates allowed", totalAvailable);
        List<Product> addedProducts = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            try {
                int randomIndex = DataHelper.getRandomNumber(0, totalAvailable - 1);
                selectProductByIndex(products, randomIndex);
                clickAddToCartButton();
                addedProducts.add(selectedProduct);
            } catch (Exception e) {
                LOG.warn("⚠️ Failed to add product #{}: {}", i, e.getMessage());
            }
        }

        LOG.info("✅ Added {} product(s) to cart", addedProducts.size());
        return addedProducts;
    }

    @Step("Navigate to Shopping Cart page via View Cart button")
    public CartPage navigateToShoppingCartPage() {
        LOG.info("🛒 Navigating to Shopping Cart page...");
        viewCartButton.shouldBe(Condition.VISIBLE, Condition.CLICKABLE);
        viewCartButton.scrollTo().click();
        LOG.debug("✅ Shopping Cart page opened.");
        return new CartPage();
    }
}
