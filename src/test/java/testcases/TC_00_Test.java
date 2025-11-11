package testcases;

import base.BaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenium.pageObjects.HomePage;

public class TC_00_Test extends BaseTest {

    @Test(description = "Verify Homepage title")
    @Description("Open homepage and verify title")
    public void testOpenHomePage() {

        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage();

        homePage.open();

        String actual = driver.getTitle();
        soft.assertTrue(actual.contains("TestArchitect"), "Homepage title mismatch!");

        softAssert.assertAll();
    }
}
