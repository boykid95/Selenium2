package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.core.log.LoggerManager;

public final class StepHelper {

    private static final Logger LOG = LoggerManager.getLogger(StepHelper.class);

    private StepHelper() {
    }

    @Step("{stepName}")
    public static void capture(String stepName, WebElement element) {
        if (element != null) {
            LOG.info("📸 Capturing step with scroll + highlight + full viewport: {}", stepName);
            AllureHelper.captureWithScrollAndHighlight(element, stepName);
        } else {
            LOG.warn("⚠️ Element is NULL for '{}', fallback to viewport", stepName);
            AllureHelper.attachScreenshot(stepName);
        }
    }

}
