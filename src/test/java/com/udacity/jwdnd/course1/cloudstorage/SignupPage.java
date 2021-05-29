package com.udacity.jwdnd.course1.cloudstorage;

import org.h2.pagestore.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    private WebElement input_firstName;

    @FindBy(id = "inputLastName")
    private WebElement input_lastName;

    @FindBy(id = "inputUsername")
    private WebElement input_username;

    @FindBy(id = "inputPassword")
    private WebElement input_password;

    @FindBy(id = "buttonSubmit")
    private WebElement button_submit;

    public SignupPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void performSignUp(String firstName, String lastName, String userName, String password){
        input_firstName.sendKeys(firstName);
        input_lastName.sendKeys(lastName);
        input_username.sendKeys(userName);
        input_password.sendKeys(password);
        button_submit.click();
    }
}
