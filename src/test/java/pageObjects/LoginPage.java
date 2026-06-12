package pageObjects;

import base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    public LoginPage(){
        super();
    }

    @FindBy(className = "btn")
    WebElement getStartedBtn;

    @FindBy(linkText = "Sign in")
    WebElement signInLink;

    @FindBy(id = "id_username")
    WebElement usernameField;

    @FindBy(id = "id_password")
    WebElement passwordField;

    @FindBy(css = "input[type='submit']")
    WebElement loginSubmitButton;

    @FindBy(className = "alert-primary")
            WebElement errorMessage;


public void clickSignInLink() {

    waitForElementToClick(signInLink, 10);
}

public void enterCredentials(String username, String password) {

    if(username!=null)
        usernameField.sendKeys(username);
    if(password!=null)
        passwordField.sendKeys(password);
}

public void clickLoginBtn() {

    waitForElementToClick(loginSubmitButton, 10);
}
    public String getErrorMessage() {
        return errorMessage.getText();
    }

}