package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import vn.agest.selenium.core.config.ConfigReader;

public class GooglePage extends BasePage {

    private static final String GOOGLE_URL = ConfigReader.get("url.google");

    @Step("Open Google homepage")
    public void openPage() {
        open(GOOGLE_URL);
    }
}
