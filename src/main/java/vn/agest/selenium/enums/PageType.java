package vn.agest.selenium.enums;

import lombok.Getter;

@Getter
public enum PageType {
    HOME_PAGE("homePageUrl", "homePageTitle");

    private final String urlKey;
    private final String titleKey;

    PageType(String urlKey, String titleKey) {
        this.urlKey = urlKey;
        this.titleKey = titleKey;
    }
}