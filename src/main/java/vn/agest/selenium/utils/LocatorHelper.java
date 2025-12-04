package vn.agest.selenium.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;

public final class LocatorHelper {

    private static final Logger LOG = LoggerManager.getLogger(LocatorHelper.class);

    private LocatorHelper() {
    }

    // ====================== DYNAMIC LOCATOR FACTORY ======================

    public static BaseElement getDynamicLocator(String locatorTemplate, Object... args) {
        String resolvedLocator = String.format(locatorTemplate, args);
        By locator = detectLocatorType(resolvedLocator);

        LOG.debug("[DynamicLocator] type={} | resolved={}", getLocatorType(locator), resolvedLocator);
        return BaseElement.el(locator, "Dynamic Element [" + resolvedLocator + "]");
    }

    public static By getDynamicBy(String locatorTemplate, Object... args) {
        String resolvedLocator = String.format(locatorTemplate, args);
        By locator = detectLocatorType(resolvedLocator);

        LOG.debug("[DynamicBy] type={} | resolved={}", getLocatorType(locator), resolvedLocator);
        return locator;
    }

    // ====================== LOCATOR DETECTION ======================

    private static By detectLocatorType(String locator) {
        if (locator == null || locator.isBlank()) {
            LOG.warn("[LocatorHelper] Empty locator string detected!");
            throw new IllegalArgumentException("Locator string cannot be null or empty.");
        }

        String lower = locator.toLowerCase().trim();
        if (lower.startsWith("xpath=")) {
            return By.xpath(locator.substring(6));
        }
        if (lower.startsWith("css=")) {
            return By.cssSelector(locator.substring(4));
        }
        if (lower.startsWith("id=")) {
            return By.id(locator.substring(3));
        }
        if (lower.startsWith("name=")) {
            return By.name(locator.substring(5));
        }

        if (locator.startsWith("//") || locator.startsWith("(//")) {
            return By.xpath(locator);
        }
        if (locator.startsWith(".") || locator.startsWith("#") ||
                locator.contains(">") || locator.contains("[") ||
                locator.contains(":nth") || locator.contains("class=")) {
            return By.cssSelector(locator);
        }

        if (locator.matches("^[A-Za-z0-9_-]+$")) {
            return By.id(locator);
        }

        LOG.warn("[LocatorHelper] Unknown locator format: '{}'. Fallback to XPath contains-text.", locator);
        return By.xpath(String.format("//*[contains(text(),'%s')]", locator));
    }

    // ====================== LOGGING SUPPORT ======================

    private static String getLocatorType(By locator) {
        String type = locator.getClass().getSimpleName();
        return type.replace("By", "");
    }
}
