package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

public class StackPage {

    WebDriver driver;

    public StackPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ---------- Locators ----------

    @FindBy(xpath = "//a[contains(@href,'stack')]")
    WebElement stackGetStartedBtn;

    @FindBy(xpath = "//a[@href='operations-in-stack']")
    WebElement operationsInStackLink;

    @FindBy(xpath = "//a[@href='implementation']")
    WebElement implementationLink;

    @FindBy(xpath = "//a[@href='stack-applications']")
    WebElement applicationsLink;

    @FindBy(xpath = "//a[contains(@href,'tryEditor')]")
    WebElement tryHereBtn;

    @FindBy(xpath = "//button[text()='Run']")
    WebElement runBtn;

    @FindBy(xpath = "//div[contains(@class,'CodeMirror')]")
    WebElement codeEditor;

    @FindBy(xpath = "//pre[@id='output']")
    WebElement output;

    @FindBy(id = "navbarDropdown")
    WebElement dataStructureDropdown;

    // ---------- Common Wait ----------

    public void clickElement(WebElement element) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(element));

        element.click();
    }

    // ---------- Actions ----------

    public void clickStackGetStarted() {
        clickElement(stackGetStartedBtn);
    }

    public void clickOperationsInStack() {
        clickElement(operationsInStackLink);
    }

    public void clickImplementation() {
        clickElement(implementationLink);
    }

    public void clickApplications() {
        clickElement(applicationsLink);
    }

    public void clickTryHere() {
        clickElement(tryHereBtn);
    }

    public void clickRun() {
        clickElement(runBtn);
    }

    public void enterPythonCode(String code) {

        codeEditor.click();

        Actions actions = new Actions(driver);

        actions.sendKeys(code).perform();
    }

    public String getOutput() {
        return output.getText();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void selectDropdown(String option) {

        clickElement(dataStructureDropdown);

        WebElement item = driver.findElement(By.linkText(option));

        clickElement(item);
    }

    public void clickStackOperation() {
    }
}