package vn.agest.selenium.core.config;

import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;

public final class WindowConfig {

    private static final Logger LOG = LoggerManager.getLogger(WindowConfig.class);

    private WindowConfig() {
    }

    public static String mode() {
        String mode = ConfigLoader.getString("window.mode");
        LOG.debug("Window mode = {}", mode);
        return mode;
    }

    public static int width() {
        int width = ConfigLoader.getInt("window.width");
        LOG.debug("Window width = {}", width);
        return width;
    }

    public static int height() {
        int height = ConfigLoader.getInt("window.height");
        LOG.debug("Window height = {}", height);
        return height;
    }
}
