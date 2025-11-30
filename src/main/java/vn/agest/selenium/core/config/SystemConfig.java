package vn.agest.selenium.core.config;

public final class SystemConfig {

    private SystemConfig() {
    }

    public static String get(String key) {
        return System.getProperty(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = System.getProperty(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }
}
