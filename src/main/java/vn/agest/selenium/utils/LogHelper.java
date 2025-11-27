package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.model.Product;

import java.util.List;

public final class LogHelper {

    private static final Logger LOG = LoggerManager.getLogger(LogHelper.class);

    private LogHelper() {
    }

    // ===================== BASIC LOGGING =====================

    @Step("Log product list: {header}")
    public static void logProductList(String header, List<Product> products) {
        if (products == null || products.isEmpty()) {
            LOG.warn("{} → No products found!", header);
            return;
        }

        int size = products.size();
        LOG.info("{} ({} item{}):", header, size, size > 1 ? "s" : "");
        for (Product p : products) {
            LOG.info("   • {} | Price: {} | Qty: {}", p.getName(),
                    String.format("%.2f", p.getPrice()), p.getQuantity());

        }
    }

    // ===================== COMPARISON LOGGING =====================

    @Step("Compare expected vs actual product lists for {context}")
    public static void logComparisonSummary(String context, List<Product> expected, List<Product> actual) {
        LOG.info("🔎 [{}] Comparing expected vs actual product lists...", context);

        if (expected == null || actual == null) {
            LOG.error("❌ One of the product lists is null. Expected={}, Actual={}", expected, actual);
            return;
        }

        LOG.info("📦 Expected ({}):", expected.size());
        expected.forEach(p -> LOG.info("   → {}", formatProduct(p)));

        LOG.info("🛒 Actual ({}):", actual.size());
        actual.forEach(p -> LOG.info("   → {}", formatProduct(p)));
    }

    // ===================== DIFF LOGGING (OPTIONAL BONUS) =====================

    @Step("Log differences between expected and actual lists for {context}")
    public static void logProductDiff(String context, List<Product> expected, List<Product> actual) {
        if (expected == null || actual == null) return;

        expected.stream()
                .filter(p -> !actual.contains(p))
                .forEach(p -> LOG.warn("❌ [{}] Missing in actual: {}", context, formatProduct(p)));

        actual.stream()
                .filter(p -> !expected.contains(p))
                .forEach(p -> LOG.warn("⚠️ [{}] Unexpected in actual: {}", context, formatProduct(p)));
    }

    // ===================== PRIVATE HELPERS =====================

    private static String formatProduct(Product p) {
        return String.format("%s | Price: %.2f | Qty: %d", p.getName(), p.getPrice(), p.getQuantity());
    }
}
