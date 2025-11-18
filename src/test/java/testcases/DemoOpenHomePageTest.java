package testcases;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenium.pageObjects.HomePage;

public class DemoOpenHomePageTest extends BaseTest {

    @Test(description = "Verify Homepage title is shown correctly")
    @Description("Open the homepage and verify the correct title is displayed")
    public void testOpenHomePage() {
        verifyHomePageTitle();
    }

    @Step("Verify homepage title matches expected")
    private void verifyHomePageTitle() {

        SoftAssert softAssert = new SoftAssert();
        HomePage homePage = new HomePage();

        homePage.open();

        String actualTitle = homePage.getPageTitle();
        String expectedTitle = homePage.getExpectedTitle();
        String currentUrl = homePage.getCurrentUrl();   // mới thêm

        Allure.addAttachment("Expected Title", expectedTitle);
        Allure.addAttachment("Actual Title", actualTitle);
        Allure.addAttachment("Current URL", currentUrl);

        softAssert.assertTrue(
                actualTitle.equals(expectedTitle),
                "\nTITLE MISMATCH\n" +
                        "Expected: " + expectedTitle + "\n" +
                        "Actual:   " + actualTitle + "\n" +
                        "URL:      " + currentUrl + "\n"
        );

        softAssert.assertAll();
    }
}
