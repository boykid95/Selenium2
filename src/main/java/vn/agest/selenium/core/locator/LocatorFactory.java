package vn.agest.selenium.core.locator;

import org.openqa.selenium.By;

import java.util.Objects;

public final class LocatorFactory {

    private LocatorFactory() {
    }

    public static By x(String template, Object... args) {
        Objects.requireNonNull(template, "XPath template must not be null");
        if (args == null || args.length == 0) {
            return By.xpath(template.trim());
        }
        return By.xpath(String.format(template, args));
    }

    public static By css(String template, Object... args) {
        Objects.requireNonNull(template, "CSS template must not be null");
        if (args == null || args.length == 0) {
            return By.cssSelector(template.trim());
        }
        return By.cssSelector(String.format(template, args));
    }

    public static By id(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return By.id(id);
    }

    public static By name(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return By.name(name);
    }
}
