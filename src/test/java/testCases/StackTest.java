package testCases;

import Base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.StackPage;

import java.time.Duration;

public class StackTest extends BaseClass {

    StackPage stackPage;
    WebDriverWait wait;

    @BeforeMethod
    public void setUpTest() {

        System.out.println("Driver from BaseClass = " + driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // =========================
        // STEP 0: OPEN APP
        // =========================
        driver.get("https://dsportalapp.herokuapp.com/");

        // wait page load
        wait.until(driver -> driver.getTitle().length() > 0);

        // =========================
        // STEP 1: LANDING PAGE GET STARTED
        // =========================
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Get Started')] | //button[contains(text(),'Get Started')]")
        )).click();

        // wait for URL change (IMPORTANT FIX)
        wait.until(ExpectedConditions.urlContains("home"));

        // =========================
        // STEP 2: DASHBOARD GET STARTED
        // =========================
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Get Started')] | //button[contains(text(),'Get Started')]")
        )).click();

        // wait for stack landing page to load
        wait.until(ExpectedConditions.urlContains("stack"));

        // =========================
        // STEP 3: INIT PAGE OBJECT
        // =========================
        stackPage = new StackPage(driver);
    }

    // =========================
    // TEST 1
    // =========================
    @Test(priority = 1)
    public void verifyStackPageNavigation() {

        stackPage.clickStackGetStarted();

        Assert.assertTrue(
                driver.getTitle().contains("Stack"),
                "Stack page title mismatch"
        );
    }

    // =========================
    // HELPER METHOD
    // =========================
    public void navigateToOperationsInStack() {

        stackPage.clickStackGetStarted();
        stackPage.clickOperationsInStack();
    }

    // =========================
    // TEST 2
    // =========================
    @Test(priority = 2)
    public void verifyOperationsInStackPage() {

        navigateToOperationsInStack();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("stack"),
                "User is not on Operations in Stack page"
        );
    }

    // =========================
    // TEST 3
    // =========================
    @Test(priority = 3)
    public void verifyTryHereNavigation() {

        navigateToOperationsInStack();
        stackPage.clickTryHere();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("try"),
                "Try Here page not opened"
        );
    }

    // =========================
    // CLEANUP
    // =========================
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}