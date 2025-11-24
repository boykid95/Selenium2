package vn.agest.selenium.enums;

import java.util.HashMap;
import java.util.Map;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private static final Map<String, BrowserType> MAP = new HashMap<>();

    static {
        for (BrowserType type : values()) {
            MAP.put(type.value.toLowerCase(), type);
        }
    }

    private final String value;

    BrowserType(String value) {
        this.value = value;
    }

    public static BrowserType fromString(String value) {
        BrowserType type = MAP.get(value.toLowerCase());
        if (type == null) {
            throw new IllegalArgumentException("Unknown browser: " + value);
        }
        return type;
    }
}
