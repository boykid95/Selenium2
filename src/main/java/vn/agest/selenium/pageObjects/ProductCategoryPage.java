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

    // ======================= LOCATORS =======================
    private static final By PRODUCT_ITEMS = By.xpath("//div[contains(@class,'ajax-content')]//div[contains(@class,'content-product')]");
    private static final By PRODUCT_TITLE = By.xpath(".//h2[contains(@class,'product-title')]/a");
    private static final By PRODUCT_PRICE = By.xpath(".//span[contains(@class,'price')]//span[contains(@class,'amount')][last()]");
    private static final By ADD_TO_CART_BUTTON = By.xpath(".//div[contains(@class,'product-details')]//a[contains(@class,'add_to_cart_button')]");
    private static final By LOADER = By.xpath("//div[@class='et-loader']");

    // ======================= VARIABLES =======================
    private WebElement selectedProductElement;
    private Product selectedProduct;

    public ProductCategoryPage() {
        super(PageType.PRODUCT_CATEGORY_PAGE);
    }

    // ======================= MAIN ACTION =======================

    @Step("Add {count} random product(s) to cart")
    public void addRandomProductToCart(int count) {
        LOG.info("🛒 Adding {} random product(s) to cart...", count);

        List<WebElement> products = getAllProducts();
        List<Integer> randomIndexes = DataHelper.getRandomIndexes(products.size(), count);

        for (Integer index : randomIndexes) {
            selectProductByIndex(products, index);
            clickAddToCartButton();
        }
    }

    // ======================= STEP 1: GET ALL PRODUCTS =======================

    @Step("Get all available products from category page")
    private List<WebElement> getAllProducts() {
        List<WebElement> products = driver.findElements(PRODUCT_ITEMS);

        if (products.isEmpty()) {
            throw new IllegalStateException("❌ No products found on category page!");
        }

        LOG.debug("Found {} products on category page.", products.size());
        return products;
    }

    // ======================= STEP 2: SELECT PRODUCT =======================

    @Step("Select product at index {index}")
    private void selectProductByIndex(List<WebElement> products, int index) {
        selectedProductElement = products.get(index);

        String name = selectedProductElement.findElement(PRODUCT_TITLE).getText().trim();
        String priceText = selectedProductElement.findElement(PRODUCT_PRICE).getText().trim();
        double price = DataHelper.parsePrice(priceText);

        selectedProduct = new Product(name, price, 1);
        LOG.info("✅ Selected product [{}]: {} | Price: {}", index, name, price);
    }

    // ======================= STEP 3: ADD TO CART =======================

    @Step("Click 'Add to Cart' button for selected product")
    private void clickAddToCartButton() {
        LOG.info("Clicking 'Add to Cart' button for '{}'", selectedProduct.getName());

        // Tìm nút Add to Cart trong sản phẩm được chọn
        WebElement addToCartButton = selectedProductElement.findElement(ADD_TO_CART_BUTTON);

        // Dùng locator gốc để chờ hiển thị (WaitHelper yêu cầu By)
        WaitHelper.waitForVisible(ADD_TO_CART_BUTTON);

        // Click trực tiếp vào phần tử con trong sản phẩm
        addToCartButton.click();

        // Chờ vòng tròn loader biến mất
        waitForLoaderToDisappear(selectedProduct);
    }


    // ======================= STEP 4: WAIT FOR LOADER =======================

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
