package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")
    private WebElement input_username;

    @FindBy(id = "inputPassword")
    private WebElement input_password;

    @FindBy(id = "button_submit")
    private WebElement button_submit;

    public LoginPage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void performLogin(String username, String password){
        input_username.sendKeys(username);
        input_password.sendKeys(password);
        button_submit.click();
    }
}
