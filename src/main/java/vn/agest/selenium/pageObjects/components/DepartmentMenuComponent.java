package vn.agest.selenium.pageObjects.components;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.enums.ProductCategory;

public class DepartmentMenuComponent {

    private static final Logger LOG = LoggerManager.getLogger(DepartmentMenuComponent.class);
    private static final String CATEGORY_LINK_XPATH =
            "//div[@class='secondary-menu-wrapper']//a[normalize-space()='%s']";
    private final BaseElement allDepartmentsMenu = new BaseElement(
            By.xpath("//div[@class='secondary-menu-wrapper']//span[text()='All departments']"),
            "All Departments Menu"
    );

    @Step("Open category: {category.displayName}")
    public void openCategory(ProductCategory category) {
        allDepartmentsMenu.moveTo();
        LOG.debug("Hovered on All Departments menu");

        BaseElement categoryLink = new BaseElement(
                By.xpath(String.format(CATEGORY_LINK_XPATH, category.getDisplayName())),
                category.getDisplayName()
        );

        categoryLink.shouldBe(Condition.VISIBLE);
        categoryLink.click();

        LOG.info("✅ Navigated to category page: {}", category.getDisplayName());
    }

}
