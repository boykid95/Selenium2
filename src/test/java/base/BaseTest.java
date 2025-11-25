package base;

import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.utils.WaitHelper;

public abstract class BaseTest {

    private static final Logger LOG = LoggerManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "remote"})
    public void setUp(ITestResult result,
                      @Optional("") String browserParam,
                      @Optional("") String remoteParam) {

        String browser = browserParam.isEmpty()
                ? ConfigLoader.getString("browser")
                : browserParam;

        String remote = remoteParam.isEmpty()
                ? ConfigLoader.getString("remote")
                : remoteParam;

        LOG.debug("Browser = {}", browser);
        LOG.debug("Remote  = {}", remote);

        DriverManager.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WaitHelper.pause(2000);
        DriverManager.quitDriver();
    }
}
