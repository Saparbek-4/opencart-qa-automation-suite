package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private By emailField = By.id("input-email");
    private By passwordField = By.id("input-password");
    private By loginButton = By.cssSelector("input[value='Login']");
    private By userIcon = By.xpath("//*[@id=\"top-links\"]/ul/li[2]/a/i");
    private By dropDownLoginButton = By.xpath("//*[@id=\"top-links\"]/ul/li[2]/ul/li[2]/a");

    public LoginPage() {
        super();
    }

    public void navigateToLoginPage() {
        click(userIcon);
        click(dropDownLoginButton);
    }

    public void login(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
    }
}