package vn.agest.selenium.core.driver;

import org.openqa.selenium.WebDriver;
import vn.agest.selenium.enums.BrowserType;

public class DriverFactory {

    public static WebDriver createInstance(String browser) {
        BrowserType type = BrowserType.fromString(browser);
        WebDriverFactory factory = switch (type) {
            case CHROME -> new ChromeDriverFactory();
            case FIREFOX -> new FirefoxDriverFactory();
            case EDGE -> new EdgeDriverFactory();
        };

        WebDriver driver = factory.createDriver();
        driver.manage().window().maximize();
        return driver;
    }

}