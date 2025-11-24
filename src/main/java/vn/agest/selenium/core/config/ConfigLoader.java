package vn.agest.selenium.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.constants.Constants;
import vn.agest.selenium.core.log.LoggerManager;

import java.io.InputStream;

public final class ConfigLoader {

    private static final Logger LOG = LoggerManager.getLogger(ConfigLoader.class);
    private static JsonNode rootNode;

    static {
        loadJson();
    }

    private ConfigLoader() {
    }

    private static void loadJson() {
        try (InputStream is = ConfigLoader.class.getClassLoader()
                .getResourceAsStream(Constants.CONFIG_FILE)) {

            if (is == null) {
                throw new IllegalStateException(Constants.CONFIG_FILE + " not found in resources");
            }

            rootNode = new ObjectMapper().readTree(is);
            LOG.info("Loaded {} successfully.", Constants.CONFIG_FILE);

        } catch (Exception e) {
            LOG.error("Failed to load {}", Constants.CONFIG_FILE, e);
            throw new RuntimeException("Error loading " + Constants.CONFIG_FILE, e);
        }
    }

    // ===================== CORE GETTERS ======================

    public static String getString(String path) {
        String sys = System.getProperty(path);
        if (sys != null) {
            LOG.info("[ConfigLoader] Override '{}' from CLI => {}", path, sys);
            return sys;
        }
        return getNode(path).asText();
    }

    public static boolean getBoolean(String path) {
        String sys = System.getProperty(path);
        if (sys != null) {
            LOG.info("[ConfigLoader] Override BOOL '{}' from CLI => {}", path, sys);
            return Boolean.parseBoolean(sys);
        }
        return getNode(path).asBoolean();
    }

    public static int getInt(String path) {
        String sys = System.getProperty(path);
        if (sys != null) {
            LOG.info("[ConfigLoader] Override INT '{}' from CLI => {}", path, sys);
            return Integer.parseInt(sys);
        }
        return getNode(path).asInt();
    }

    private static JsonNode getNode(String path) {
        JsonNode node = rootNode;
        for (String key : path.split("\\.")) {
            node = node.get(key);
            if (node == null) {
                LOG.error("Missing config key: {}", path);
                throw new IllegalArgumentException("Config key not found: " + path);
            }
        }
        return node;
    }

    // ===================== SHORTCUTS ======================

    public static String getBrowser() {
        return getString("browser");
    }

    public static boolean isRemote() {
        return getBoolean("remote");
    }

    public static String getGridUrl() {
        return getString("gridUrl");
    }

    public static boolean isHeadless() {
        return getBoolean("headless");
    }

    public static int timeout(String key) {
        return getInt("timeouts." + key);
    }

    public static String windowMode() {
        return getString("window.mode");
    }

    public static int windowWidth() {
        return getInt("window.width");
    }

    public static int windowHeight() {
        return getInt("window.height");
    }
}
