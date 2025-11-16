package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.driver.DriverManager;

public abstract class BaseTest {

    private static final Logger LOG = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {

        String browser = ConfigLoader.getString("browser");

        LOG.info("========== START TEST: {} ==========", result.getMethod().getMethodName());
        LOG.info("Browser = {}", browser);

        DriverManager.initDriver(browser);
    }
}
