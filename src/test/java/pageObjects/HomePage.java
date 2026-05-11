package pageObjects;

import Base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

      public HomePage() {
      super();
     }
    // ---------------- LOCATORS ----------------

   @FindBy(xpath = "//a[contains(@href,'array') and @class='dropdown-item']")
    WebElement dropdwnArrayElement;

    @FindBy(xpath = "//div[@class='alert alert-primary']")
    WebElement alertMsg;

    @FindBy(xpath = "//a[contains(text(),'Data Structures')]")
    WebElement drpdwnDS;

    // ---------------- METHODS ----------------

    public void clickDropdownDS() {
        drpdwnDS.click();
    }

    public void clickDropdownElements(String drpEle) {
        if (drpEle.equals("Arrays")) {
            dropdwnArrayElement.click();
        }
    }

    public String getAlertMsg() {
       return alertMsg.getText();
    }
}




