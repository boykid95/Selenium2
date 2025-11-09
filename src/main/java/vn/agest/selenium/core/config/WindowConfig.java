package vn.agest.selenium.core.config;

public final class WindowConfig {

    private WindowConfig() {
    }

    // Get window mode: maximize | fullscreen | custom
    public static String mode() {
        String mode = ConfigReader.getOrDefault("window.mode", "maximize");
        return mode == null ? "maximize" : mode.trim();
    }

    // Custom width — return -1 when invalid (DriverWindowManager will fallback)
    public static int width() {
        int w = ConfigReader.getInt("window.width", -1);
        return w > 0 ? w : -1;
    }

    // Custom height — return -1 when invalid
    public static int height() {
        int h = ConfigReader.getInt("window.height", -1);
        return h > 0 ? h : -1;
    }
}
