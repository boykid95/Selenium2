package base;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import vn.agest.selenium.core.config.ConfigReader;
import vn.agest.selenium.core.driver.DriverManager;

public abstract class BaseTest {

    private static final Logger LOG = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Step("Setup browser and driver")
    public void setUp(Object[] testData) {
        String browser = ConfigReader.getOrDefault("browser", "chrome");

        LOG.info("========== START TEST: {} ==========", getTestName(testData));
        LOG.info("Browser = {}", browser);

        DriverManager.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    @Step("Quit browser and cleanup")
    public void tearDown(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {
            LOG.error("TEST FAILED: {}", result.getName());
        } else {
            LOG.info("TEST PASSED: {}", result.getName());
        }

        DriverManager.quitDriver();
        LOG.info("========== END TEST ==========\n");
    }

    private String getTestName(Object[] data) {
        if (data != null && data.length > 0) {
            return data[0].toString();
        }
        return "Unnamed Test";
    }
}
