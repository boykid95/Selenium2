package vn.agest.selenium.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {
    ELECTRONIC_COMPONENTS_SUPPLIES(
            "Electronic Components & Supplies",
            "electronic-components-supplies",
            "Electronic Components & Supplies – TestArchitect Sample Website"
    );

    private final String displayName;
    private final String urlPath;
    private final String expectedTitle;
}
