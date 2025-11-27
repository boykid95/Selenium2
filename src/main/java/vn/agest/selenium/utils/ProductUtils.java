package vn.agest.selenium.utils;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.model.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ProductUtils {

    private static final Logger LOG = LoggerManager.getLogger(ProductUtils.class);

    private ProductUtils() {
    }

    @Step("Merge product list by name (sum quantities & prices)")
    public static List<Product> mergeList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            LOG.warn("⚠️ mergeList() called with null or empty list");
            return new ArrayList<>();
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

        List<Product> result = new ArrayList<>(merged.values());
        LOG.info("✅ mergeList() completed: {} → {} unique item(s)", products.size(), result.size());
        return result;
    }
}
