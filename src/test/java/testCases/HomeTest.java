package testCases;

import Base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.HomePage;

import java.time.Duration;

public class HomeTest extends BaseClass {

    HomePage homePage;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {

        // FORCE driver initialization from BaseClass
        super.setUp();   // ⚠️ only works if BaseClass has setUp()

        driver.get("https://dsportalapp.herokuapp.com/home");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Get Started')] | //a[contains(text(),'Get Started')]")
        )).click();

        homePage = new HomePage(driver);
    }

    @Test
    public void verifyArrayModule() {

        homePage.clickGetStartedForModule("Array");

        System.out.println(driver.getCurrentUrl());
    }

    @Test
    public void verifyStackModule() {

        homePage.clickGetStartedForModule("Stack");

        System.out.println(driver.getCurrentUrl());
    }

    @Test
    public void verifyQueueModule() {

        homePage.clickGetStartedForModule("Queue");

        System.out.println(driver.getCurrentUrl());
    }
}