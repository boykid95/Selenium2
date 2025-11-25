package vn.agest.selenium.pageObjects;

import vn.agest.selenium.enums.PageType;

public class HomePage extends BasePage {

    public HomePage() {
        super(PageType.HOME_PAGE);
    }

    public void open() {
        super.open();
        closePopupIfPresent();
        acceptCookieIfVisible();
    }
}

