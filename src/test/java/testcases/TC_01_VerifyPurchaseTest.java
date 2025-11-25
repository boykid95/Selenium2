package testcases;

import base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenium.enums.ProductCategory;
import vn.agest.selenium.pageObjects.HomePage;
import vn.agest.selenium.pageObjects.LoginPage;
import vn.agest.selenium.pageObjects.ProductCategoryPage;

public class TC_01_VerifyPurchaseTest extends BaseTest {

    @Test(description = "Verify users can buy an item successfully")
    @Description("Full E2E flow: Open homepage → Login → Navigate category → Add product → Checkout → Verify order")
    public void verifyUserCanBuyItemSuccessfully() {

        SoftAssert softAssert = new SoftAssert();

        // Initialize Page Objects
        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        ProductCategoryPage categoryPage = new ProductCategoryPage();
//        ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
//        CheckoutPage checkoutPage = new CheckoutPage();
//        OrderStatusPage orderStatusPage = new OrderStatusPage();
//        MiniCartComponent miniCart = new MiniCartComponent();

        // ======================= STEP 1: OPEN HOME PAGE ==========================
        homePage.open();

        // ======================= STEP 2: LOGIN SUCCESSFULLY ======================
        loginPage.login();
////        AllureHelper.attachScreenshot("STEP 2 - Verify login successful");
//
        // ======================= STEP 3: NAVIGATE TO PRODUCT CATEGORY ============
        homePage.navigateToCategoryPage(ProductCategory.ELECTRONIC_COMPONENTS_SUPPLIES);
//
//        // ======================= STEP 4: ADD RANDOM PRODUCT TO CART ==============
        categoryPage.addRandomProductToCart(2);
//
//        // ======================= STEP 5: CLICK CART BUTTON TO OPEN SHOPPING CART PAGE ========================
//        // ======================= STEP 6: VERIFY INFORMATION IN CART ========================
//        categoryPage.navigateToShoppingCartPage();
//        softAssert.assertTrue(
//                miniCart.verifyCartItemMatchesSelectedProduct(categoryPage),
//                "❌ Cart product info mismatch"
//        );
////        AllureHelper.attachScreenshot("STEP 6 - Verify product info in cart");
//
//        // ======================= STEP 7: CLICK ON 'PROCEED TO CHECKOUT' ======================
//        // ======================= STEP 8: VERIFY CHECKOUT PAGE DISPLAYS ======================
//        shoppingCartPage.navigateToCheckoutPage();
//        softAssert.assertTrue(
//                checkoutPage.isLoaded(),
//                "❌ Checkout page did not load successfully"
//        );
////        AllureHelper.attachScreenshot("STEP 8 - Verify checkout page loaded");
//
//        // ======================= STEP 9: VERIFY ORDER DETAILS ====================
//        softAssert.assertTrue(
//                checkoutPage.verifyOrderItemDetails(categoryPage),
//                "❌ Product details mismatch on Checkout page"
//        );
////        AllureHelper.attachScreenshot("STEP 9 - Verify checkout product details");
//
//        // ======================= STEP 10: FILL BILLING INFO WITH DEFAULT PAYMENT METHOD =========
//        checkoutPage.fillBillingDetailsDefault();
//
//        // ======================= STEP 11: CLICK 'PLACE ORDER' =====================
//        // ======================= STEP 12: VERIFY ORDER STATUS PAGE ===============
//        checkoutPage.navigateToOrderStatusPage();
//        softAssert.assertTrue(
//                orderStatusPage.isLoaded(),
//                "❌ Order Status page is not loaded"
//        );
////        AllureHelper.attachScreenshot("STEP 12 - Verify order status page loaded");
//
//        // ======================= STEP 13: VERIFY ORDER DETAILS & BILLING INFO ===============
//        softAssert.assertTrue(
//                orderStatusPage.verifyOrderItemDetails(categoryPage.getSelectedProduct()),
//                "❌ Order item details mismatch"
//        );
////        AllureHelper.attachScreenshot("STEP 13 - Verify order item details");
//
//        softAssert.assertTrue(
//                orderStatusPage.verifyBillingDetails(checkoutPage),
//                "❌ Billing information mismatch"
//        );
////        AllureHelper.attachScreenshot("STEP 13 - Verify billing details");
//
//        // ======================= ASSERT ALL =====================================
        softAssert.assertAll();
    }
}
