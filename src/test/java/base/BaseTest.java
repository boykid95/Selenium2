package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import vn.agest.selenium.core.config.ConfigLoader;
import vn.agest.selenium.core.driver.DriverManager;

public abstract class BaseTest {

    private static final Logger LOG = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "remote"})
    public void setUp(ITestResult result,
                      @Optional("") String browserParam,
                      @Optional("") String remoteParam) {

        String browser = !browserParam.isEmpty()
                ? browserParam
                : ConfigLoader.getString("browser");

        String remote = !remoteParam.isEmpty()
                ? remoteParam
                : ConfigLoader.getString("remote");

        LOG.info("========== START TEST: {} ==========", result.getMethod().getMethodName());
        LOG.info("Browser = {}", browser);
        LOG.info("Remote  = {}", remote);

        System.setProperty("browser", browser);
        System.setProperty("remote", remote);

        DriverManager.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        LOG.info("========== END TEST: {} - Status: {} ==========",
                result.getMethod().getMethodName(),
                getStatusName(result.getStatus()));

        DriverManager.quitDriver();
    }

    private String getStatusName(int status) {
        switch (status) {
            case ITestResult.SUCCESS:
                return "PASSED";
            case ITestResult.FAILURE:
                return "FAILED";
            case ITestResult.SKIP:
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }
}
