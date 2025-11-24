package vn.agest.selenium.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.constants.Constants;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.model.PageInfo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class PageTitleLoader {

    private static final Logger LOG = LoggerManager.getLogger(PageTitleLoader.class);

    private static final Map<PageType, PageInfo> PAGE_MAP = new HashMap<>();

    static {
        loadJson();
    }

    private PageTitleLoader() {
    }

    private static void loadJson() {
        try (InputStream is = PageTitleLoader.class.getClassLoader()
                .getResourceAsStream(Constants.PAGE_TITLES_FILE)) {

            if (is == null) {
                throw new IllegalStateException(
                        "Cannot find " + Constants.PAGE_TITLES_FILE + " in resources!");
            }

            JsonNode root = new ObjectMapper().readTree(is);
            JsonNode pagesNode = root.get("pages");

            for (PageType type : PageType.values()) {
                JsonNode node = pagesNode.get(type.name());
                if (node == null) {
                    LOG.warn("No entry found for PageType: {}", type.name());
                    continue;
                }

                String url = node.get("url").asText();
                String title = node.get("title").asText();

                PAGE_MAP.put(type, new PageInfo(url, title));
            }

            LOG.info("Page titles successfully loaded from {}", Constants.PAGE_TITLES_FILE);

        } catch (Exception ex) {
            LOG.error("Failed to load page titles!", ex);
            throw new RuntimeException(ex);
        }
    }

    public static PageInfo get(PageType type) {
        PageInfo info = PAGE_MAP.get(type);

        if (info == null) {
            throw new IllegalArgumentException(
                    "PageInfo not found for PageType: " + type.name());
        }

        return info;
    }
}
