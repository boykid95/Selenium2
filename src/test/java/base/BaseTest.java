package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import vn.agest.selenium.core.ConfigReader;
import vn.agest.selenium.core.DriverFactory;

public class BaseTest {
    protected WebDriver driver;
    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver(ConfigReader.get("browser"));
    }
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
