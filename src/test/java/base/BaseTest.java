package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.config.ConfigReader;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = ConfigReader.getOrDefault("browser", "chrome");
        DriverManager.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
