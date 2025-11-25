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

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(ProductCategoryPage.class);

    private static final By PRODUCT_ITEMS = By.xpath("//div[contains(@class,'ajax-content')]//div[contains(@class,'content-product')]");
    private static final By PRODUCT_TITLE = By.xpath(".//h2[contains(@class,'product-title')]/a");
    private static final By PRODUCT_PRICE = By.xpath(".//span[contains(@class,'price')]//span[contains(@class,'amount')][last()]");
    private static final By ADD_TO_CART_BUTTON = By.xpath(".//div[contains(@class,'product-details')]//a[contains(@class,'add_to_cart_button')]");
    private static final By LOADER = By.xpath("//div[@class='et-loader']");
    private static final By VIEW_CART_BUTTON = By.xpath("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]");


    private BaseElement selectedProductElement;
    private Product selectedProduct;

    public ProductCategoryPage() {
        super(PageType.PRODUCT_CATEGORY_PAGE);
    }

    @Step("Navigate to Shopping Cart page via View Cart button")
    public ShoppingCartPage navigateToShoppingCartPage() {
        LOG.info("🛒 Clicking 'View Cart' button to open Shopping Cart page...");
        BaseElement viewCartButton = BaseElement.el(VIEW_CART_BUTTON, "View Cart Button");
        viewCartButton.scrollTo();
        viewCartButton.click();
        LOG.debug("✅ Shopping Cart page opened successfully.");
        return new ShoppingCartPage();
    }

    @Step("Add {count} random product(s) to cart")
    public void addRandomProductToCart(int count) {
        LOG.info("🛒 Adding {} random product(s) to cart...", count);

        List<BaseElement> products = getAllProducts();
        List<Integer> randomIndexes = DataHelper.getRandomIndexes(products.size(), count);

        for (Integer index : randomIndexes) {
            selectProductByIndex(products, index);
            clickAddToCartButton();
        }
    }

    @Step("Get all available products from category page")
    private List<BaseElement> getAllProducts() {
        LOG.debug("🔍 Fetching product list...");
        WaitHelper.waitForVisible(PRODUCT_ITEMS);

        List<BaseElement> products = driver.findElements(PRODUCT_ITEMS).stream()
                .map(e -> BaseElement.el(e, "Product Item"))
                .collect(Collectors.toList());

        if (products.isEmpty()) {
            LOG.error("❌ No products found on category page!");
            throw new IllegalStateException("No products found on category page!");
        }

        LOG.debug("✅ Found {} products on category page.", products.size());
        return products;
    }

    @Step("Select product at index {index}")
    private void selectProductByIndex(List<BaseElement> products, int index) {
        selectedProductElement = products.get(index);

        String name = selectedProductElement.findChild(PRODUCT_TITLE).getText().trim();
        String priceText = selectedProductElement.findChild(PRODUCT_PRICE).getText().trim();
        double price = DataHelper.parsePrice(priceText);

        selectedProduct = new Product(name, price, 1);
        LOG.info("✅ Selected product [{}]: {} | Price: {}", index, name, price);
    }

    @Step("Click 'Add to Cart' button for selected product")
    private void clickAddToCartButton() {
        LOG.info("Clicking 'Add to Cart' button for '{}'", selectedProduct.getName());

        BaseElement addToCartButton = selectedProductElement.findChild(ADD_TO_CART_BUTTON);

        addToCartButton.scrollTo();
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

    // ======================= GETTER =======================

    public Product getSelectedProduct() {
        return selectedProduct;
    }
}
