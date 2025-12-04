package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;

import java.util.Random;

public final class DataHelper {

    private static final Logger LOG = LoggerManager.getLogger(DataHelper.class);
    private static final Random RANDOM = new Random();

    private DataHelper() {
    }

    // ===================== RANDOM UTILS =====================

    @Step("Generate random number from {min} to {max}")
    public static int getRandomNumber(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Min must be less than Max");
        }
        int result = RANDOM.nextInt((max - min) + 1) + min;
        LOG.debug("Generated random number: {}", result);
        return result;
    }

    // ===================== PARSE / FORMAT =====================

    @Step("Parse price text '{priceText}' to numeric value")
    public static double parsePrice(String priceText) {
        try {
            if (priceText.contains("THIS IS A DEMO STORE")) {
                return 0.0;
            }
            priceText = priceText.replaceAll("[^\\d.]", "");
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            LOG.error("Invalid price format: {}", priceText);
            return 0.0;
        }
    }

    @Step("Parse quantity text '{qtyText}' to integer")
    public static int parseQuantity(String qtyText) {
        try {
            int value = Integer.parseInt(qtyText.trim());
            LOG.debug("Parsed quantity '{}' → {}", qtyText, value);
            return value;
        } catch (NumberFormatException e) {
            LOG.warn("⚠️ Invalid quantity '{}', fallback to 1", qtyText);
            return 1;
        }
    }

}
