package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import vn.agest.selenium.core.driver.DriverManager;
import vn.agest.selenium.utils.AllureHelper;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("=== TEST START: " + result.getName() + " ===");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        AllureHelper.attachText("Test Result", "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        AllureHelper.attachScreenshot("Failure Screenshot");
        AllureHelper.attachPageSource();
        AllureHelper.attachText("Error Message", result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        AllureHelper.attachText("Test Result", "SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=== TEST SUITE FINISHED ===");
        DriverManager.quitDriver();
    }
}
