package vn.agest.selenium.enums;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String value;

    BrowserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BrowserType fromString(String value) {
        for (BrowserType type : BrowserType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown browser: " + value);
    }
}
