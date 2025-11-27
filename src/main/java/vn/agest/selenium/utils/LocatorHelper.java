package vn.agest.selenium.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;

public final class LocatorHelper {

    private static final Logger LOG = LoggerManager.getLogger(LocatorHelper.class);

    private LocatorHelper() {}

    // ====================== CORE DYNAMIC LOCATOR ======================

    public static BaseElement getDynamicLocator(String locatorTemplate, Object... args) {
        String resolvedLocator = String.format(locatorTemplate, args);
        By locator = detectLocatorType(resolvedLocator);

        LOG.debug("[DynamicLocator] Resolved: {}", resolvedLocator);
        return BaseElement.el(locator, "Dynamic Element [" + resolvedLocator + "]");
    }

    public static By getDynamicBy(String locatorTemplate, Object... args) {
        String resolvedLocator = String.format(locatorTemplate, args);
        By locator = detectLocatorType(resolvedLocator);

        LOG.debug("[DynamicBy] Resolved: {}", resolvedLocator);
        return locator;
    }

    // ====================== PRIVATE DETECTION ======================

    private static By detectLocatorType(String locator) {
        if (locator.startsWith("//") || locator.startsWith("(//")) {
            return By.xpath(locator);
        } else if (locator.startsWith(".") || locator.contains(">") || locator.contains("#") || locator.contains("[")) {
            return By.cssSelector(locator);
        } else if (locator.startsWith("id=")) {
            return By.id(locator.replace("id=", ""));
        } else if (locator.startsWith("name=")) {
            return By.name(locator.replace("name=", ""));
        } else {
            LOG.warn("[LocatorHelper] Unknown locator type for '{}'. Defaulting to ID.", locator);
            return By.id(locator);
        }
    }
}
