package testCases;

import Base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HomePage;

public class HomeTest extends BaseClass {

    // ---------------- DROPDOWN TEST ----------------

    @Test
    public void verifyHomePage() {

        HomePage home = new HomePage(driver);

        home.clickGetStarted();   // navigation step

        // now actual validations
    }
    @Test
    public void verifyDropdownWarningMessage() {

        HomePage homePage = new HomePage(driver);

        homePage.clickDropdownDS();
        homePage.clickDropdownElements("Arrays");

        String actualAlertMsg = homePage.getAlertMsg();

        Assert.assertEquals(actualAlertMsg, "You are not logged in");
    }

    // ---------------- GET STARTED TESTS ----------------

    @Test
    public void verifyArrayGetStarted() {

        HomePage homePage = new HomePage(driver);

        homePage.clickGetStartedBtn("Array");

        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("array"));
    }

    @Test
    public void verifyStackGetStarted() {

        HomePage homePage = new HomePage(driver);

        homePage.clickGetStartedBtn("Stack");

        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("stack"));
    }

    @Test
    public void verifyQueueGetStarted() {

        HomePage homePage = new HomePage(driver);

        homePage.clickGetStartedBtn("Queue");

        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("queue"));
    }

    @Test
    public void verifyTreeGetStarted() {

        HomePage homePage = new HomePage(driver);

        homePage.clickGetStartedBtn("Tree");

        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("tree"));
    }

    @Test
    public void verifyGraphGetStarted() {

        HomePage homePage = new HomePage(driver);

        homePage.clickGetStartedBtn("Graph");

        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("graph"));
    }

    @Test
    public void verifyDSIntroGetStarted() {

        HomePage homePage = new HomePage(driver);

        homePage.clickGetStartedBtn("Data Structures - Introduction");

        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("data"));
    }
}