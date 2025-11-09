package vn.agest.selenium.core.driver;

import org.openqa.selenium.WebDriver;

import java.net.URL;

public interface WebDriverFactory {

    WebDriver createLocalDriver();

    WebDriver createRemoteDriver(URL gridUrl);
}
