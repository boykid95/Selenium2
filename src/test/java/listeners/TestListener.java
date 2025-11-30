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
        LOG.info("🚀 TEST STARTED: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("✅ TEST PASSED: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("❌ TEST FAILED: {}", result.getName(), result.getThrowable());

        // 🖼 Capture full screenshot
        AllureHelper.attachScreenshot("Failure Screenshot");

        // 📄 Attach page source
        AllureHelper.attachPageSource();

        // 🧾 Attach error log text
        if (result.getThrowable() != null) {
            String stackTrace = getStackTrace(result.getThrowable());
            AllureHelper.attachText("Failure Stack Trace", stackTrace);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("⚠️ TEST SKIPPED: {}", result.getName());
        AllureHelper.attachText("Skipped Reason", "Test was skipped: " +
                (result.getThrowable() != null ? result.getThrowable().getMessage() : "no reason"));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOG.warn("⚠️ TEST FAILED WITHIN SUCCESS PERCENTAGE: {}", result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        LOG.info("🧩 TEST SUITE STARTED: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("🏁 TEST SUITE FINISHED: {}", context.getName());
    }

    // ==================== PRIVATE HELPERS ====================

    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
        }
        return sb.toString();
    }
}
