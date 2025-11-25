package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import vn.agest.selenium.core.config.CredentialsLoader;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.utils.WaitHelper;

public class LoginPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(LoginPage.class);

    // ======================= ELEMENTS =======================
    private final BaseElement usernameField = BaseElement.el(By.id("username"), "Username Field");
    private final BaseElement passwordField = BaseElement.el(By.id("password"), "Password Field");
    private final BaseElement loginButton = BaseElement.el(By.cssSelector("button[name='login']"), "Login Button");
    private final BaseElement logoutLink = BaseElement.el(By.cssSelector("a.logout"), "Logout Link");

    public LoginPage() {
        super(PageType.LOGIN_PAGE);
    }

    // ======================= ACTIONS =======================

    @Step("Login with valid credentials")
    public void login() {
        LOG.info("Attempting to login with valid credentials...");
        openLoginPage();
        enterUsername(CredentialsLoader.getUsername("validUser"));
        enterPassword(CredentialsLoader.getPassword("validUser"));
        clickLoginButton();
    }

    @Step("Open Login Page")
    private void openLoginPage() {
        driver.get(pageInfo.url());
        LOG.debug("Opened Login page: {}", pageInfo.url());
    }

    @Step("Enter username: {username}")
    private void enterUsername(String username) {
        WaitHelper.waitForVisible(usernameField.getLocator());
        usernameField.scrollTo();
        usernameField.setText(username);
        LOG.debug("Entered username.");
    }

    @Step("Enter password")
    private void enterPassword(String password) {
        WaitHelper.waitForVisible(passwordField.getLocator());
        passwordField.setText(password);
        LOG.debug("Entered password.");
    }

    @Step("Click Login Button")
    private void clickLoginButton() {
        loginButton.scrollTo();
        loginButton.click();
        LOG.debug("Clicked Login button.");
    }

    @Step("Logout current user")
    public void logout() {
        if (logoutLink.isDisplayed()) {
            logoutLink.click();
            LOG.info("User logged out.");
        } else {
            LOG.debug("Logout link not found; user may already be logged out.");
        }
    }
}
