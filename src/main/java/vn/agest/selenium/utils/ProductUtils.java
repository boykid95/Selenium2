package vn.agest.selenium.utils;

import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class ProductUtils {

    private static final Logger LOG = LoggerManager.getLogger(ProductUtils.class);

    private ProductUtils() {
    }

    public static List<Product> mergeList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            LOG.warn("⚠️ mergeList() called with null or empty list");
            return Collections.emptyList();
        }

        Map<String, Product> merged = new LinkedHashMap<>();
        for (Product p : products) {
            if (p == null) continue;
            p.normalize();

            merged.merge(p.getName().toLowerCase(),
                    new Product(p.getName(), p.getPrice(), p.getQuantity()),
                    (oldP, newP) -> {
                        oldP.setQuantity(oldP.getQuantity() + newP.getQuantity());
                        return oldP;
                    });
        }

        LOG.debug("✅ mergeList() completed: {} → {} unique item(s)", products.size(), merged.size());
        return new ArrayList<>(merged.values());
    }

    // ===================== 🔹 PRICE CALCULATION =====================
    public static BigDecimal calculateUnitPrice(BigDecimal total, int qty) {
        if (qty <= 0) qty = 1;
        return (qty > 1)
                ? total.divide(BigDecimal.valueOf(qty), 2, RoundingMode.HALF_UP)
                : total;
    }
}
