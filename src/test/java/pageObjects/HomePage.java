package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ---------------- LOCATORS ----------------

    @FindBy(xpath = "//a[@href='/home']")
    WebElement getStartedBtn;

    @FindBy(xpath = "//a[contains(text(),'Data Structures')]")
    WebElement drpdwnDS;

    @FindBy(xpath = "//a[contains(@href,'array') and @class='dropdown-item']")
    WebElement dropdwnArrayElement;

    @FindBy(xpath = "//div[@class='alert alert-primary']")
    WebElement alertMsg;

    // ---------------- ACTIONS ----------------

    public void clickGetStarted() {

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));

        WebElement btn = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(getStartedBtn)
        );

        btn.click();
    }

//    public void clickDropdownDS() {
//        drpdwnDS.click();
//    }

    public void clickDropdownDS() {
        WebElement ds = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(drpdwnDS));

        ds.click();
    }

//    public void clickDropdownElements(String module) {
//
//        clickDropdownDS();
//
//        WebElement element = driver.findElement(
//                By.xpath("//a[@class='dropdown-item' and text()='" + module + "']")
//        );
//
//        element.click();
//    }

    public void clickDropdownElements(String module) {

        clickDropdownDS();

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));

        WebElement element = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[@class='dropdown-item' and text()='" + module + "']")
                )
        );

        element.click();
    }

//    public void clickGetStartedBtn(String module) {
//
//        WebElement getStartedBtn = driver.findElement(
//                By.xpath("//div[contains(text(),'" + module + "')]/following::a[text()='Get Started']")
//        );
//
//        getStartedBtn.click();
//    }

    public void clickGetStartedBtn(String module) {

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));

        WebElement getStartedBtn = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(text(),'" + module + "')]/following::a[text()='Get Started']")
                )
        );

        getStartedBtn.click();
    }

    public String getAlertMsg() {
        return alertMsg.getText();
    }
}