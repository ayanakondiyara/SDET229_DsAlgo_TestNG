package pageObjects;

import base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class HomePage extends BasePage {

    public HomePage() {
        super();
    }
    // ---------------- LOCATORS ----------------

    @FindBy(xpath = "//a[@href =\"/home\"]")
    WebElement GetStartedbtn;

    @FindBy(xpath = "//a[@href=\"/login\"]")
    WebElement SignInLink;

    @FindBy(xpath = " //a[@href=\"/register\"]")
    WebElement RegisterLink;



//-----------------Methods-----------------------

    public void clickGetStartedBtn() {
        waitForElementToClick(GetStartedbtn, 10);
    }

    public void clickSignInLink() {
        waitForElementToClick(SignInLink, 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    public void clickRegisterLink() {
        waitForElementToClick(RegisterLink, 10);
    }
}