package testcases;

import base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenium.enums.ProductCategory;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.pageObjects.*;

import java.util.List;

public class TC01_VerifyPurchaseTest extends BaseTest {

    @Test(description = "Verify users can buy an item successfully")
    @Description("Full E2E flow: Open homepage → Login → Navigate category → Add product → Checkout → Verify order")
    public void verifyUserCanBuyItemSuccessfully() {

        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        ProductCategoryPage categoryPage = new ProductCategoryPage();
        CartPage cartPage = new CartPage();
        CheckoutPage checkoutPage = new CheckoutPage();
//        OrderStatusPage orderStatusPage = new OrderStatusPage();

        // ======================= STEP 1: OPEN HOME PAGE ==========================
        homePage.open();

        // ======================= STEP 2: LOGIN SUCCESSFULLY ======================
        loginPage.login();
//        AllureHelper.attachScreenshot("STEP 2 - Verify login successful");

        // ======================= STEP 3: NAVIGATE TO PRODUCT CATEGORY ============
        homePage.navigateToCategoryPage(ProductCategory.ELECTRONIC_COMPONENTS_SUPPLIES);

        // ======================= STEP 4: ADD RANDOM PRODUCT TO CART ==============
        List<Product> selectedProducts = categoryPage.addRandomProductsToCart(4);

        // ======================= STEP 5: CLICK CART BUTTON TO OPEN SHOPPING CART PAGE ========================
        // ======================= STEP 6: VERIFY INFORMATION IN CART ========================
        categoryPage.navigateToShoppingCartPage();
        List<Product> actualCartProducts = cartPage.getCartProductInfo();
        softAssert.assertEquals(
                actualCartProducts, selectedProducts,
                "❌ Cart product info mismatch"
        );
////        AllureHelper.attachScreenshot("STEP 6 - Verify product info in cart");
//
//        // ======================= STEP 7: CLICK ON 'PROCEED TO CHECKOUT' ======================
//        // ======================= STEP 8: VERIFY CHECKOUT PAGE DISPLAYS ======================
        cartPage.navigateToCheckoutPage();
        softAssert.assertTrue(
                checkoutPage.isLoaded(),
                "❌ Checkout page did not load successfully"
        );
////        AllureHelper.attachScreenshot("STEP 8 - Verify checkout page loaded");
//
//        // ======================= STEP 9: VERIFY ORDER DETAILS ====================
        List<Product> actualCheckoutItems = checkoutPage.getCheckoutProductInfo();

        softAssert.assertEquals(
                actualCheckoutItems,
                actualCartProducts,
                "❌ Checkout product list does not match cart items"
        );
//
////        AllureHelper.attachScreenshot("STEP 9 - Verify checkout product details");
//
//        // ======================= STEP 10: FILL BILLING INFO WITH DEFAULT PAYMENT METHOD =========
//        BillingInfo expectedBillingInfo = checkoutPage.fillBillingDetailsDefault();
//
//        // ======================= STEP 11: CLICK 'PLACE ORDER' =====================
//        // ======================= STEP 12: VERIFY ORDER STATUS PAGE ===============
//        checkoutPage.navigateToOrderStatusPage();
//
//        softAssert.assertTrue(
//                orderStatusPage.isLoaded(),
//                "❌ Order Status page did not load successfully"
//        );
////        AllureHelper.attachScreenshot("STEP 12 - Verify order status page loaded");
//
//        // ======================= STEP 13: VERIFY ORDER DETAILS & BILLING INFO ===============
//        List<Product> actualOrderItems = orderStatusPage.getOrderProductInfo();
//        BillingInfo actualBillingInfo = orderStatusPage.getDisplayedBillingInfo();
//
//        softAssert.assertEquals(
//                actualOrderItems,
//                actualCheckoutItems,
//                "❌ Ordered product details mismatch"
//        );
////        AllureHelper.attachScreenshot("STEP 13 - Verify order item details");
//
//        softAssert.assertEquals(actualBillingInfo, expectedBillingInfo, "❌ Billing info mismatch");
//        AllureHelper.attachScreenshot("STEP 13 - Verify billing details");

        // ======================= ASSERT ALL =====================================
        softAssert.assertAll();
    }
}
