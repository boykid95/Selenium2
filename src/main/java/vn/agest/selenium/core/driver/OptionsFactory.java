package vn.agest.selenium.core.driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import vn.agest.selenium.core.config.ConfigReader;

public class OptionsFactory {

    public static ChromeOptions chrome() {
        ChromeOptions options = new ChromeOptions();

        options.setAcceptInsecureCerts(true);

        options.addArguments("--disable-notifications");

        if (ConfigReader.getBoolean("headless", false)) {
            options.addArguments("--headless=new");
        }

        return options;
    }

    public static FirefoxOptions firefox() {
        FirefoxOptions options = new FirefoxOptions();

        options.setAcceptInsecureCerts(true);

        if (ConfigReader.getBoolean("headless", false)) {
            options.addArguments("-headless");
        }

        return options;
    }

    public static EdgeOptions edge() {
        EdgeOptions options = new EdgeOptions();

        options.setAcceptInsecureCerts(true);

        if (ConfigReader.getBoolean("headless", false)) {
            options.addArguments("--headless=new");
        }

        return options;
    }
}
