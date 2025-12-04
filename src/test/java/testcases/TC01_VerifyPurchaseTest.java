package testcases;

import base.BaseTest;
import io.qameta.allure.Description;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenium.enums.BillingProfile;
import vn.agest.selenium.enums.ProductCategory;
import vn.agest.selenium.model.BillingInfo;
import vn.agest.selenium.model.Product;
import vn.agest.selenium.pageObjects.*;
import vn.agest.selenium.utils.ProductUtils;
import vn.agest.selenium.utils.StepHelper;

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
        OrderStatusPage orderStatusPage = new OrderStatusPage();

        homePage.open();
        loginPage.login();
        homePage.navigateToCategoryPage(ProductCategory.ELECTRONIC_COMPONENTS_SUPPLIES);

        List<Product> selectedProducts = categoryPage.addRandomProductsToCart(1);
        selectedProducts = ProductUtils.mergeList(selectedProducts);

        categoryPage.navigateToShoppingCartPage();
        List<Product> actualCartProducts = cartPage.getCartProductInfo();
        actualCartProducts = ProductUtils.mergeList(actualCartProducts);

        softAssert.assertEquals(actualCartProducts, selectedProducts, "❌ Cart product info mismatch");
        WebElement cartTable = cartPage.getCartTableElement();
        StepHelper.capture("STEP 6 - Verify product info in cart", cartTable);

        cartPage.navigateToCheckoutPage();
        softAssert.assertTrue(checkoutPage.isLoaded(), "❌ Checkout page did not load successfully");
        StepHelper.capture("STEP 8 - Verify checkout page loaded", checkoutPage.getCheckoutHeaderElement());

        List<Product> actualCheckoutItems = checkoutPage.getCheckoutProductInfo();
        actualCheckoutItems = ProductUtils.mergeList(actualCheckoutItems);
        softAssert.assertEquals(actualCheckoutItems, actualCartProducts, "❌ Checkout product list mismatch");
        StepHelper.capture("STEP 9 - Verify checkout product details", checkoutPage.getOrderSummaryTableElement());

        BillingInfo checkoutBillingInfo = checkoutPage.fillBillingDetailsDefault();
        softAssert.assertEquals(checkoutBillingInfo, BillingProfile.DEFAULT.getInfo(), "❌ Billing info mismatch");

        checkoutPage.navigateToOrderStatusPage();
        softAssert.assertTrue(orderStatusPage.isLoaded(), "❌ Order Status page did not load successfully");
        StepHelper.capture("STEP 12 - Verify order status page loaded", orderStatusPage.getOrderElement());

        List<Product> actualOrderItems = orderStatusPage.getOrderProductInfo();
        BillingInfo orderBillingInfo = orderStatusPage.getDisplayedBillingInfo();

        softAssert.assertEquals(actualOrderItems, actualCheckoutItems, "❌ Ordered product details mismatch");
        StepHelper.capture("STEP 13 - Verify order item details", orderStatusPage.getOrderDetailsElement());

        softAssert.assertEquals(orderBillingInfo, checkoutBillingInfo, "❌ Billing info mismatch");
        StepHelper.capture("STEP 13 - Verify billing details", orderStatusPage.getBillingAddressElement());

        softAssert.assertAll();
    }
}
