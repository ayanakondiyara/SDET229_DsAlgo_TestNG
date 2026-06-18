package pageObjects;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LinkedListPage extends BasePage {

    //WebDriver driver;
    //WebDriverWait wait;

    public LinkedListPage() {
        //this.driver = driver;
        super();
    }

    // ================= LOCATORS =================

    private By linkedListGetStarted =
            By.xpath("//a[@href='linked-list']");

    private By tryHereButton =
            By.xpath("//a[contains(text(),'Try here')]");

    private By practiceButton =
            By.linkText("Practice Questions");

    private By runButton =
            By.xpath("//button[contains(text(),'Run')]");

    private By outputBox =
            By.id("output");

    private By subModuleLink(String SubModule) {
        return By.xpath("//a[contains(text(),'" + SubModule + "')]");
    }

    // ================= ACTIONS =================


    public void clickLinkedListLink() {
        driver.findElement(linkedListGetStarted).click();
    }

    public void clickSubmodule(String SubModule) {
        System.out.println("Current URL = " + driver.getCurrentUrl()); //added for debugging
        System.out.println("Looking for = " + SubModule); // added for debugging
        driver.findElement(subModuleLink(SubModule)).click();
    }

    public void clickTryHere() {
        System.out.println("Before Try Here URL = " + driver.getCurrentUrl());

        driver.findElement(tryHereButton).click();

        System.out.println("After Try Here URL = " + driver.getCurrentUrl());
    }

    public void runCodeInTryEditor(String code) {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);",
                code
        );

        driver.findElement(runButton).click();
    }

    public String getTryEditorOutput() {
        return driver.findElement(outputBox).getText();
    }

    public void clickPracticeQuestions() {
        driver.findElement(practiceButton).click();
    }

    /*public void runCode(String code) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".CodeMirror")));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);",
                code
        );

        driver.findElement(runButton).click();
    }

    public String getOutput() {

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (Exception e) {
            return driver.findElement(outputBox).getText().trim();
        }
    }*/
}