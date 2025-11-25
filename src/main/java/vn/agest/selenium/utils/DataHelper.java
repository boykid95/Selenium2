package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;

import java.util.ArrayList;
import java.util.List;
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

    @Step("Generate {count} unique random indexes from total {total}")
    public static List<Integer> getRandomIndexes(int total, int count) {
        if (total <= 0 || count <= 0) {
            throw new IllegalArgumentException("Total and count must be positive");
        }

        List<Integer> indexes = new ArrayList<>();
        while (indexes.size() < Math.min(count, total)) {
            int index = RANDOM.nextInt(total);
            if (!indexes.contains(index)) {
                indexes.add(index);
            }
        }
        LOG.debug("Generated random indexes: {}", indexes);
        return indexes;
    }

    // ===================== PARSE / FORMAT =====================

    @Step("Parse price text '{priceText}' to numeric value")
    public static double parsePrice(String priceText) {
        try {
            double value = Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
            LOG.debug("Parsed price '{}' → {}", priceText, value);
            return value;
        } catch (NumberFormatException e) {
            LOG.error("Failed to parse price: {}", priceText, e);
            throw new RuntimeException("Invalid price format: " + priceText, e);
        }
    }

    @Step("Format value {value} into currency string")
    public static String formatCurrency(double value) {
        String formatted = String.format("$%.2f", value);
        LOG.debug("Formatted {} → {}", value, formatted);
        return formatted;
    }

    // ===================== DATA GENERATION =====================

    @Step("Generate random email address")
    public static String generateRandomEmail() {
        String email = "user" + getRandomNumber(1000, 9999) + "@test.com";
        LOG.debug("Generated random email: {}", email);
        return email;
    }

    @Step("Generate random string of length {length}")
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        String result = builder.toString();
        LOG.debug("Generated random string ({}): {}", length, result);
        return result;
    }
}
