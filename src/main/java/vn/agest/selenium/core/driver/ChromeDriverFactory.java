package vn.agest.selenium.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.agest.selenium.core.config.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;

public class ChromeDriverFactory implements WebDriverFactory {

    @Override
    public WebDriver createDriver() {

        boolean isRemote = ConfigReader.getBoolean("remote", false);
        String gridUrl = ConfigReader.get("gridUrl");

        if (isRemote) {
            try {
                return new RemoteWebDriver(new URL(gridUrl), OptionsFactory.chrome());
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
            }
        }

        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(OptionsFactory.chrome());
    }
}
