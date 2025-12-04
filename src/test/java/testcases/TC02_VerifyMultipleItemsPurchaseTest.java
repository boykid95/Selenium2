package testcases;

import base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenium.core.constants.Constants;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.pageObjects.*;
import vn.agest.selenium.utils.ProductUtils;
import vn.agest.selenium.utils.StepHelper;

import java.util.List;

public class TC02_VerifyMultipleItemsPurchaseTest extends BaseTest {

    @Test(description = "Verify users can buy multiple items successfully")
    @Description("Full flow: Open site → Login → Go to shop → Add multiple products → Verify cart → Checkout → Verify order confirmation")
    public void verifyUserCanBuyMultipleItemsSuccessfully() {

        SoftAssert softAssert = new SoftAssert();

        // ====== PAGE OBJECTS ======
        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        ShopPage shopPage = new ShopPage();
        CartPage cartPage = new CartPage();
        CheckoutPage checkoutPage = new CheckoutPage();
        OrderStatusPage orderStatusPage = new OrderStatusPage();

        // ================= STEP 1–3: OPEN, LOGIN, GO TO SHOP PAGE =================
        homePage.open();
        loginPage.login();
        homePage.navigateToShopPage();

        // ================= STEP 4: ADD MULTIPLE PRODUCTS TO CART =================
        List<Product> selectedProducts = shopPage.addRandomProductsToCart(5);
        selectedProducts = ProductUtils.mergeList(selectedProducts);

        // ================= STEP 5: VERIFY ALL ITEMS IN CART =================
        shopPage.navigateToShoppingCartPage();
        List<Product> cartProducts = cartPage.getCartProductInfo();
        cartProducts = ProductUtils.mergeList(cartProducts);

        softAssert.assertEquals(
                cartProducts,
                selectedProducts,
                "❌ Cart products do not match selected items."
        );
        StepHelper.capture("STEP 5 - Verify selected products in cart", cartPage.getCartTableElement());

        // ================= STEP 6: CHECKOUT & CONFIRM ORDER =================
        cartPage.navigateToCheckoutPage();
        checkoutPage.navigateToOrderStatusPage();

        // ================= STEP 7: VERIFY ORDER CONFIRMATION MESSAGE =================
        String actualMessage = orderStatusPage.getOrderConfirmationMessage();
        softAssert.assertEquals(
                actualMessage,
                Constants.ORDER_CONFIRMATION,
                "❌ Order confirmation message mismatch"
        );
        StepHelper.capture("STEP 7 - Verify order confirmation message", orderStatusPage.getOrderElement());

        List<Product> orderedItems = orderStatusPage.getOrderProductInfo();
        softAssert.assertEquals(
                orderedItems,
                cartProducts,
                "❌ Ordered products do not match cart items."
        );

        StepHelper.capture("STEP 7 - Verify purchased items", orderStatusPage.getOrderDetailsElement());

        softAssert.assertAll();
    }
}
