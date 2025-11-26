package vn.agest.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.model.Product;

import java.util.List;

public class LogHelper {

    private static final Logger LOG = LogManager.getLogger(LogHelper.class);

    public static void logProductList(String header, List<Product> products) {
        if (products == null || products.isEmpty()) {
            LOG.warn("{} → No products found!", header);
            return;
        }

        LOG.info("{} ({} item{}):", header, products.size(), products.size() > 1 ? "s" : "");
        for (Product p : products) {
            LOG.info("   • {} | Price: {} | Qty: {}", p.getName(), p.getPrice(), p.getQuantity());
        }
    }

    public static void logComparisonSummary(String context, List<Product> expected, List<Product> actual) {
        LOG.info("🔎 [{}] Comparing expected vs actual product lists...", context);
        LOG.info("Expected ({}):", expected.size());
        expected.forEach(p -> LOG.info("   → {}", formatProduct(p)));

        LOG.info("Actual ({}):", actual.size());
        actual.forEach(p -> LOG.info("   → {}", formatProduct(p)));
    }

    private static String formatProduct(Product p) {
        return String.format("%s | Price: %.2f | Qty: %d", p.getName(), p.getPrice(), p.getQuantity());
    }
}
