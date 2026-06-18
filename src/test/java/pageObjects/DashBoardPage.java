package pageObjects;

import base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class DashBoardPage extends BasePage {

    public DashBoardPage() {

        super();
    }

    //---------------------------Locators------------------------------

    @FindBy(xpath = "//div[@class='content']/h1")
    WebElement labelTxtH1;

    @FindBy(xpath = "//div[@class='content']/p")
    WebElement labelTxtP;

    @FindBy(xpath = "//button[text()='Get Started']")
    WebElement btnGetStarted;

    @FindBy(xpath = "//a[text()='Data Structures']")
    WebElement drpdwnDS;

    @FindBy(xpath = "//a[@class='dropdown-item']")
    List<WebElement> drpdwnDSOptions;

    @FindBy(xpath = "//a[text()='NumpyNinja']")
    WebElement NumpyNinjaText;

    @FindBy(xpath = "//a[text()=' Register ']")
    WebElement registerText;

    @FindBy(xpath = "//a[text()='Sign in']")
    WebElement signInText;

    @FindBy(xpath = "//h5[contains(text(),'Intro')]")
    WebElement dsLabel;

    @FindBy(xpath = "//a[contains(@href,'data')]")
    WebElement DsGetStartedbtn;

    @FindBy(xpath = "//h5[contains(text(),'Array')]")
    WebElement arrayLabel;

    @FindBy(xpath = "//a[contains(@href,'array') and contains(text(),'Get')]")
    WebElement ArrGetStartedbtn;

    @FindBy(xpath = "//h5[contains(text(),'List')]")
    WebElement listLabel;

    @FindBy(xpath = "//a[contains(@href,'list') and contains(text(),'Get')]")
    WebElement listGetStartedbtn;

    @FindBy(xpath = "//h5[contains(text(),'Stack')]")
    WebElement stackLabel;

    @FindBy(xpath = "//a[contains(@href,'stack') and contains(text(),'Get')]")
    WebElement StackGetStartedbtn;

    @FindBy(xpath = "//h5[contains(text(),'Que')]")
    WebElement queueLabel;

    @FindBy(xpath = "//a[contains(@href,'que') and contains(text(),'Get')]")
    WebElement QueGetStartedbtn;

    @FindBy(xpath = "//h5[contains(text(),'Tree')]")
    WebElement treeLabel;

    @FindBy(xpath = "//a[contains(@href,'tree') and contains(text(),'Get')]")
    WebElement TreeGetStartedbtn;

    @FindBy(xpath = "//h5[contains(text(),'Graph')]")
    WebElement graphLabel;

    @FindBy(xpath = "//a[contains(@href,'graph') and contains(text(),'Get')]")
    WebElement GraphGetStartedbtn;

    //   -------------Methods------------------------

    public void clickSigninLink() {
        waitForElementToClick(signInText, 10);
    }
    public void clickRegister(){
        waitForElementToClick(registerText, 10);
    }

}