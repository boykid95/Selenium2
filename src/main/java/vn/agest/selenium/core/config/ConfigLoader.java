package vn.agest.selenium.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.enums.PageType;

import java.io.InputStream;

public final class ConfigLoader {

    private static final Logger LOG = LogManager.getLogger(ConfigLoader.class);
    private static JsonNode rootNode;

    static {
        loadJson();
    }

    private ConfigLoader() {
    }

    private static void loadJson() {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream("config.json")) {

            if (is == null) {
                throw new IllegalStateException("config.json not found in resources");
            }

            rootNode = new ObjectMapper().readTree(is);
            LOG.info("Loaded config.json successfully.");

        } catch (Exception e) {
            LOG.error("Failed to load config.json", e);
            throw new RuntimeException("Error loading config.json", e);
        }
    }

    public static String getString(String path) {
        String sysValue = System.getProperty(path);
        if (sysValue != null) {
            LOG.info("[ConfigLoader] Override '{}' from CLI => {}", path, sysValue);
            return sysValue;
        }
        return getNode(path).asText();
    }

    public static boolean getBoolean(String path) {
        String sysValue = System.getProperty(path);
        LOG.error("ConfigLoader.getBoolean('{}') sys='{}' json='{}'",
                path, sysValue, getNode(path).asBoolean());
        
        if (sysValue != null) {
            LOG.info("[ConfigLoader] Override '{}' from CLI => {}", path, sysValue);
            return Boolean.parseBoolean(sysValue);
        }
        return getNode(path).asBoolean();
    }

    public static int getInt(String path) {
        String sysValue = System.getProperty(path);
        if (sysValue != null) {
            LOG.info("[ConfigLoader] Override '{}' from CLI => {}", path, sysValue);
            return Integer.parseInt(sysValue);
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

    public static String getPageUrl(PageType pageType) {
        return getString("pages." + pageType.name() + ".url");
    }

    public static String getPageTitle(PageType pageType) {
        return getString("pages." + pageType.name() + ".title");
    }

    public static String getBrowser() {
        return getString("browser");
    }

    public static boolean isRemote() {
        return getBoolean("remote");
    }

    public static int timeout(String key) {
        return getInt("timeouts." + key);
    }
}
