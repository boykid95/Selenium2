package vn.agest.selenium.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.constants.Constants;
import vn.agest.selenium.core.log.LoggerManager;

import java.io.InputStream;

public final class CredentialsLoader {

    private static final Logger LOG = LoggerManager.getLogger(CredentialsLoader.class);
    private static JsonNode rootNode;

    static {
        loadJson();
    }

    private CredentialsLoader() {
    }

    private static void loadJson() {
        try (InputStream is = CredentialsLoader.class.getClassLoader()
                .getResourceAsStream(Constants.CREDENTIALS_FILE)) {

            if (is == null) {
                throw new IllegalStateException(Constants.CREDENTIALS_FILE + " not found in resources");
            }

            rootNode = new ObjectMapper().readTree(is);
            LOG.debug("Loaded {} successfully.", Constants.CREDENTIALS_FILE);

        } catch (Exception e) {
            LOG.error("Failed to load {}", Constants.CREDENTIALS_FILE, e);
            throw new RuntimeException("Error loading " + Constants.CREDENTIALS_FILE, e);
        }
    }

    public static String getUsername(String userKey) {
        return getNode(userKey + ".username").asText();
    }

    public static String getPassword(String userKey) {
        return getNode(userKey + ".password").asText();
    }

    private static JsonNode getNode(String path) {
        JsonNode node = rootNode;
        for (String key : path.split("\\.")) {
            node = node.get(key);
            if (node == null) {
                LOG.error("Missing credentials key: {}", path);
                throw new IllegalArgumentException("Credentials key not found: " + path);
            }
        }
        return node;
    }
}
