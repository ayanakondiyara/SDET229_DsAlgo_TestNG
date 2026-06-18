package pageObjects;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * ArrayPage — Page Object Model for Array module
 */
public class ArrayPage extends BasePage {

    // ================= Constructor =================
    public ArrayPage() {
        //this.driver = driver;
        super();
    }

    // ================= Locators =================

    //private By arrayGetStarted = By.xpath("//a[contains(text(),'Get Started')]");
    //private By arrayLink = By.xpath("//a[contains(text(),'Array')]");
    private By arrayLink =
            By.xpath("//a[@href='array']");
    private By tryHereButton =
            By.xpath("//a[contains(text(),'Try here') or contains(text(),'Try Here')]");
    private By runButton =
            By.xpath("//button[contains(text(),'Run')]");
    private By outputBox =
            By.id("output");

    private By practiceButton =
            By.xpath("//a[contains(text(),'Practice Questions')]");
    //private By submitButton = By.xpath("//button[contains(text(),'Submit')]");
    private By submitButton =
            By.cssSelector("#answer_form input[type='submit']");

    // Practice inputs (adjust if your DOM differs)
    private By codeEditor = By.xpath("//textarea | //pre");
    private By practiceOutput = By.id("output");

    private By searchArrayQuestion =
            By.xpath("//a[contains(text(),'Search the array')]");

    private By maxConsecutiveOnesQuestion =
            By.xpath("//a[contains(text(),'Max Consecutive Ones')]");

    private By evenDigitsQuestion =
            By.xpath("//a[contains(text(),'Find Numbers with Even Number of Digits')]");

    private By sortedArrayQuestion =
            By.xpath("//a[contains(text(),'Squares of  a Sorted Array')]");

    private By questionLink(String question) {
        return By.xpath("//a[contains(text(),'" + question + "')]");
    }

    private By subModuleLink(String subModule) {
        return By.xpath("//a[contains(text(),'" + subModule + "')]");
    }

    // ================= Actions =================

    public void clickArrayLink() {
        System.out.println("Step 1: Clicking Array Get Started");
        driver.findElement(arrayLink).click();
        System.out.println("URL after Array click = " + driver.getCurrentUrl());
    }


    public void clickSubmodule(String subModule) {

        System.out.println("Current URL = " + driver.getCurrentUrl()); //added for debugging
        System.out.println("Looking for = " + subModule); // added for debugging

        driver.findElement(subModuleLink(subModule)).click();
    }

    /*public void clickTryHere() {
        driver.findElement(tryHereButton).click();
    } */

    public void clickTryHere() {

        System.out.println("Before Try Here URL = " + driver.getCurrentUrl());

        driver.findElement(tryHereButton).click();

        System.out.println("After Try Here URL = " + driver.getCurrentUrl());
    }

    /*public void runCodeInTryEditor(String code) {
        driver.findElement(codeEditor).clear();
        driver.findElement(codeEditor).sendKeys(code);
        driver.findElement(runButton).click();
    } */

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

    /*public void clickPracticeButton(String text) {
        driver.findElement(practiceButton).click();
    }*/

    public void clickPracticeButton(String text) {

        System.out.println("Before Practice Questions URL = "
                + driver.getCurrentUrl());

        driver.findElement(practiceButton).click();

        System.out.println("After Practice Questions URL = "
                + driver.getCurrentUrl());
    }

    /*public void clickQuestion(String question) {
        driver.findElement(questionLink(question)).click();
    }*/

   /* public void clickQuestion(String question) {

        System.out.println("Current URL before question click = "
                + driver.getCurrentUrl());

        System.out.println("Looking for question = "
                + question);

        driver.findElement(questionLink(question)).click();
    } */

    public void clickQuestion(String question) {

        System.out.println("Before question click URL = "
                + driver.getCurrentUrl());

        driver.findElement(questionLink(question)).click();

        System.out.println("After question click URL = "
                + driver.getCurrentUrl());
    }
   /* public void clickQuestion(String question) {

        System.out.println("Before question click URL = "
                + driver.getCurrentUrl());

        switch (question) {

            case "Question 1":
                driver.findElement(searchArrayQuestion).click();
                break;

            case "Question 2":
                driver.findElement(maxConsecutiveOnesQuestion).click();
                break;

            case "Question 3":
                driver.findElement(evenDigitsQuestion).click();
                break;

            case "Question 4":
                driver.findElement(sortedArrayQuestion).click();
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown question: " + question);
        }

        System.out.println("After question click URL = "
                + driver.getCurrentUrl());
    }*/

    /*public void runPracticeCode(String code) {
        driver.findElement(codeEditor).clear();
        driver.findElement(codeEditor).sendKeys(code);
        driver.findElement(runButton).click();
    }*/

    public void runPracticeCode(String code) {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);",
                code
        );

        driver.findElement(runButton).click();
    }

    public String getPracticeOutput() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        // Case 1: Alert (most common for wrong code)
        try {
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            String text = alert.getText().trim();
            alert.accept();

            return text;

        } catch (Exception ignored) {
        }

        // Case 2: Output box
        try {
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));

            return wait2.until(
                    ExpectedConditions.visibilityOfElementLocated(practiceOutput)
            ).getText().trim();

        } catch (Exception e) {
            return "";
        }
    }

    /*public void submitPracticeCode(String code) {
        driver.findElement(codeEditor).clear();
        driver.findElement(codeEditor).sendKeys(code);
        driver.findElement(submitButton).click();
    }*/


    public void submitPracticeCode(String code) {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);",
                code
        );

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        submitButton));

        submitBtn.click();
    }

    public String getSubmissionMessage() {
        return getPracticeOutput();
    }
}