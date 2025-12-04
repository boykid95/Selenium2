package vn.agest.selenium.pageObjects;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.config.CredentialsLoader;
import vn.agest.selenium.core.log.LoggerManager;
import vn.agest.selenium.elements.BaseElement;
import vn.agest.selenium.enums.PageType;
import vn.agest.selenium.utils.WaitHelper;

public class LoginPage extends BasePage {

    private static final Logger LOG = LoggerManager.getLogger(LoginPage.class);

    // ======================= ELEMENTS =======================
    private final BaseElement usernameField = $id("username", "Username Field");
    private final BaseElement passwordField = $id("password", "Password Field");
    private final BaseElement loginButton = $css("button[name='login']", "Login Button");

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
        loginButton.scrollTo().click();
        LOG.debug("Clicked Login button.");
    }
}
