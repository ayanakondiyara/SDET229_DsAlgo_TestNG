package pageObjects;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegistrationPage extends BasePage {
    public RegistrationPage(){

        super();
    }
//------------- Locators-----------

    @FindBy(xpath = "//input[@id='id_username']")
    WebElement usernameTxt;

    @FindBy(xpath = "//input[@id='id_password1']")
    WebElement passwordTxt;

    @FindBy(xpath = "//input[@id='id_password2']")
    WebElement confirmPwdTxt;

    @FindBy(xpath = "//input[@type='submit']")
    WebElement registerBtn;

    @FindBy(xpath = "//a[text()='Login '] ")
    WebElement loginLink;

 //------------Methods----------------

    public void enterValues(String username, String password, String confirmPwd) {
        if (username != null)
            usernameTxt.sendKeys(username);
        if (password != null)
            passwordTxt.sendKeys(password);
        confirmPwdTxt.sendKeys(confirmPwd);
    }

    public void enterUsername(String username) {
        if (username != null)
            usernameTxt.sendKeys(username);
    }

    //public void enterValues(String username, String password) {
        //if (username != null)
            //usernameTxt.sendKeys(username);
        //if (password != null)
           // passwordTxt.sendKeys(password);
   // }

    public void clickRegisterBtn() {

        waitForElementToClick(registerBtn, 10);
    }

    public String validateUsernameTooltipMsg() {

        return getTooltipMsg(usernameTxt);
    }

    public String validatePasswordTooltipMsg() {

        return getTooltipMsg(passwordTxt);
    }

    public String validateConfirmPwdTooltipMsg() {

        return getTooltipMsg(confirmPwdTxt);
    }

    public void clickLoginInRegForm() {

        waitForElementToClick(loginLink, 10);
    }

    public void register(String username, String password, String confirmPwd) {
        enterValues(username, password, confirmPwd);
        clickRegisterBtn();
    }

    public void enterValues(WebElement element, String value) {
        element.clear();
        element.sendKeys(value == null ? "" : value);
    }

    public String waitForAlertMessage() {
        return getWait(10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")))
                .getText();
    }

}

