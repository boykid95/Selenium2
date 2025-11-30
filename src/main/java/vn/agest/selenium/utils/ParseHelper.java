package vn.agest.selenium.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ParseHelper {

    private ParseHelper() {
    }

    // ===================== 🔹 BASIC TEXT HANDLING =====================

    public static String safeLine(String[] lines, int index) {
        return (lines != null && index < lines.length) ? lines[index].trim() : "";
    }

    public static String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty())
            return new String[]{"", ""};

        String[] parts = fullName.trim().split("\\s+");
        String first = parts[0];
        String last = parts.length > 1 ? parts[parts.length - 1] : "";
        return new String[]{first, last};
    }

    // ===================== 🔹 EXTRACTORS =====================

    public static String extractPhone(String text) {
        if (text == null) return "";
        Matcher m = Pattern.compile("(\\+?\\d[\\d\\s]{8,15})").matcher(text);
        return m.find() ? m.group(1).replaceAll("\\s+", "") : "";
    }

    public static String extractEmail(String text) {
        if (text == null) return "";
        Matcher m = Pattern.compile("[\\w\\.-]+@[\\w\\.-]+").matcher(text);
        return m.find() ? m.group(0) : "";
    }

    // ===================== 🔹 PRICE & QUANTITY =====================

    public static BigDecimal parsePriceToBigDecimal(String text) {
        if (text == null || text.isEmpty()) return BigDecimal.ZERO;
        String numeric = text.replaceAll("[^\\d.,]", "").replace(",", "");
        try {
            return new BigDecimal(numeric).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public static double toDoublePrice(String text) {
        return parsePriceToBigDecimal(text).doubleValue();
    }

    public static int extractQuantity(String text) {
        if (text == null) return 1;
        Matcher m = Pattern.compile("×\\s*(\\d+)").matcher(text);
        return m.find() ? Integer.parseInt(m.group(1)) : 1;
    }

    public static int parseQuantityFromText(String text) {
        return extractQuantity(text);
    }

    // ===================== 🔹 CITY / ADDRESS PARSER =====================

    public static String[] splitCityLine(String cityLine) {
        if (cityLine == null || cityLine.isEmpty())
            return new String[]{"", "", ""};

        String city = "";
        String state = "";
        String postcode = "";

        String[] mainParts = cityLine.split(",", 2);
        city = mainParts[0].trim();

        if (mainParts.length > 1) {
            String[] rest = mainParts[1].trim().split("\\s+");
            if (rest.length > 0) state = rest[0].trim();
            if (rest.length > 1) postcode = rest[1].trim();
        }

        return new String[]{city, state, postcode};
    }

    // ===================== 🔹 BILLING TEXT PARSING =====================

    public static String normalizeBillingText(String text) {
        if (text == null) return "";
        return text.replaceAll("(\\d{5})\\s*(\\d{9,15})", "$1\n$2").trim();
    }

    public static List<String> cleanTextLines(String text) {
        List<String> clean = new ArrayList<>();
        if (text == null || text.isEmpty()) return clean;
        for (String line : text.split("\\n")) {
            if (!line.trim().isEmpty()) clean.add(line.trim());
        }
        return clean;
    }
}
