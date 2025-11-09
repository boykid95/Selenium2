package testcases;

import base.BaseTest;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.agest.selenium.pageObjects.GooglePage;

public class TC_00_Test extends BaseTest {

    @Test(description = "Verify Google title")
    @Description("Open Google homepage and verify its page title")
    public void testGoogleHomePage() {

        GooglePage google = new GooglePage();

        google.openPage();

        Assert.assertTrue(
                google.getTitle().contains("Google"),
                "Page title does not contain 'Google'"
        );
    }
}
