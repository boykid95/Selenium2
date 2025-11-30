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

}
