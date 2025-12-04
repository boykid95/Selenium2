package vn.agest.selenium.model;

import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.log.LoggerManager;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Product {

    private static final Logger LOG = LoggerManager.getLogger(Product.class);

    private String name;
    private double price;
    private int quantity;

    @Step("Normalize product fields")
    public Product normalize() {
        if (name != null) name = name.trim().replaceAll("\\s+", " ");
        else name = "";

        if (price < 0) {
            LOG.warn("⚠️ Invalid price < 0 for '{}', reset to 0", name);
            price = 0;
        }

        if (quantity < 1) {
            LOG.warn("⚠️ Invalid quantity < 1 for '{}', reset to 1", name);
            quantity = 1;
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return Double.compare(that.price, price) == 0 &&
                quantity == that.quantity &&
                name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), price, quantity);
    }

    @Override
    public String toString() {
        return String.format("%s | Price: %.2f | Qty: %d", name, price, quantity);
    }
}
