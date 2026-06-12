package pageObjects;

import Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.sql.Driver;

public class HomePage extends BasePage {

    public HomePage(WebDriver Driver) {
        super();
    }

    // ---------------- Dropdown Elements ----------------

    @FindBy(xpath = "//a[contains(@href,'array') and @class='dropdown-item']")
    WebElement dropdwnArrayElement;

    @FindBy(xpath = "//a[contains(@href,'list') and @class='dropdown-item']")
    WebElement dropdwnListElement;

    @FindBy(xpath = "//a[contains(@href,'stack') and @class='dropdown-item']")
    WebElement dropdwnStackElement;

    @FindBy(xpath = "//a[contains(@href,'que') and @class='dropdown-item']")
    WebElement dropdwnQueElement;

    @FindBy(xpath = "//a[contains(@href,'tree') and @class='dropdown-item']")
    WebElement dropdwnTreeElement;

    @FindBy(xpath = "//a[contains(@href,'graph') and @class='dropdown-item']")
    WebElement dropdwnGraphElement;

    @FindBy(xpath = "//button[text()='Get Started']")
    WebElement mainGetStartedBtn;

    // ---------------- Main Dropdown ----------------

    @FindBy(xpath = "//a[contains(text(),'Data Structures')]")
    WebElement drpdwnDS;

    // ---------------- Alert Message ----------------

    @FindBy(xpath = "//div[@class='alert alert-primary']")
    WebElement alertMsg;

    // ---------------- Get Started Buttons ----------------

    @FindBy(xpath = "(//a[text()='Get Started'])[1]")
    WebElement btnDsGetStarted;

    @FindBy(xpath = "(//a[text()='Get Started'])[2]")
    WebElement btnArrGetStarted;

    @FindBy(xpath = "(//a[text()='Get Started'])[3]")
    WebElement btnlistGetStarted;

    @FindBy(xpath = "(//a[text()='Get Started'])[4]")
    WebElement btnStackGetStarted;

    @FindBy(xpath = "(//a[text()='Get Started'])[5]")
    WebElement btnQueGetStarted;

    @FindBy(xpath = "(//a[text()='Get Started'])[6]")
    WebElement btnTreeGetStarted;

    @FindBy(xpath = "(//a[text()='Get Started'])[7]")
    WebElement btnGraphGetStarted;

    // ---------------- Methods ----------------

    public void clickDropdownDS() {
        waitForElementToClick(drpdwnDS, 10);
    }

    public void clickMainGetStarted() {
        mainGetStartedBtn.click();
    }

    public void clickDropdownElements(String drpEle) {

        switch (drpEle) {

            case "Arrays":
                waitForElementToClick(dropdwnArrayElement, 10);
                break;

            case "Linked List":
                waitForElementToClick(dropdwnListElement, 10);
                break;

            case "Stack":
                waitForElementToClick(dropdwnStackElement, 10);
                break;

            case "Queue":
                waitForElementToClick(dropdwnQueElement, 10);
                break;

            case "Tree":
                waitForElementToClick(dropdwnTreeElement, 10);
                break;

            case "Graph":
                waitForElementToClick(dropdwnGraphElement, 10);
                break;

            default:
                throw new IllegalArgumentException("Invalid Element");
        }
    }

    public void clickGetStartedBtn(String module) {

        switch (module) {

            case "Data Structures - Introduction":
                waitForElementToClick(btnDsGetStarted, 10);
                break;

            case "Array":
                waitForElementToClick(btnArrGetStarted, 10);
                break;

            case "Linked List":
                waitForElementToClick(btnlistGetStarted, 10);
                break;

            case "Stack":
                waitForElementToClick(btnStackGetStarted, 10);
                break;

            case "Queue":
                waitForElementToClick(btnQueGetStarted, 10);
                break;

            case "Tree":
                waitForElementToClick(btnTreeGetStarted, 10);
                break;

            case "Graph":
                waitForElementToClick(btnGraphGetStarted, 10);
                break;

            default:
                throw new IllegalArgumentException("Invalid Element");
        }
    }

    public String getAlertMsg() {
        return alertMsg.getText();
    }

    public void clickGetStartedForModule(String array) {
    }
}