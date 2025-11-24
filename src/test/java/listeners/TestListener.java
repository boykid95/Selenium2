package listeners;

import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.utils.AllureHelper;

public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("=== TEST START: {} ===", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("=== TEST PASSED: {} ===", result.getName());
        AllureHelper.attachText("Test Result", "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("=== TEST FAILED: {} ===", result.getName(), result.getThrowable());

        AllureHelper.attachScreenshot("Failure Screenshot");
        AllureHelper.attachPageSource();

        if (result.getThrowable() != null) {
            AllureHelper.attachText("Error Message", result.getThrowable().toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("=== TEST SKIPPED: {} ===", result.getName());
        AllureHelper.attachText("Test Result", "SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("=== TEST SUITE FINISHED ===");
    }
}
