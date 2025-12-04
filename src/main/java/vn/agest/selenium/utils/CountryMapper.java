package vn.agest.selenium.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.constants.Constants;
import vn.agest.selenium.core.log.LoggerManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class CountryMapper {

    private static final Logger LOG = LoggerManager.getLogger(CountryMapper.class);

    private static final Map<String, String> COUNTRY_CODE_MAP = new HashMap<>();
    private static final Map<String, String> US_STATE_MAP = new HashMap<>();

    static {
        loadCountryData();
    }

    private CountryMapper() {
    }

    private static void loadCountryData() {
        final String resourcePath = Constants.COUNTRIES_FILE;

        try (InputStream is = CountryMapper.class.getClassLoader().getResourceAsStream(resourcePath)) {

            if (is == null) {
                LOG.error("❌ Cannot find '{}'. Make sure it exists under src/main/resources or src/test/resources.", resourcePath);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            JsonNode countries = root.path("countries");
            if (countries.isObject()) {
                countries.fields().forEachRemaining(entry ->
                        COUNTRY_CODE_MAP.put(entry.getKey().toUpperCase(), entry.getValue().asText())
                );
            }

            JsonNode states = root.path("us_states");
            if (states.isObject()) {
                states.fields().forEachRemaining(entry ->
                        US_STATE_MAP.put(entry.getKey().toUpperCase(), entry.getValue().asText())
                );
            }

            LOG.info("✅ Loaded {} countries and {} US states successfully.",
                    COUNTRY_CODE_MAP.size(), US_STATE_MAP.size());

        } catch (Exception e) {
            LOG.error("❌ Failed to load '{}' due to: {}", resourcePath, e.getMessage());
        }
    }

    public static String resolveCountry(String code) {
        if (code == null || code.isBlank()) return "";

        String upper = code.trim().toUpperCase();

        if (US_STATE_MAP.containsKey(upper)) {
            return "United States (US)";
        }

        if (COUNTRY_CODE_MAP.containsKey(upper)) {
            return COUNTRY_CODE_MAP.get(upper);
        }

        if (upper.matches("^[A-Z]{2}$")) {
            return "United States (US)";
        }

        return code;
    }
}
