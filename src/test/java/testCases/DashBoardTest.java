package testCases;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.DashBoardPage;
import pageObjects.HomePage;

public class DashBoardTest extends BaseClass {

private DashBoardPage DbPage;
private HomePage home;

    @BeforeMethod
    public void setUpPages() {
        super.setUp();
        // Initialize page objects
        home = new HomePage();
        DbPage = new DashBoardPage();

        // Navigate to the page where Sign in / Register exist
           // REQUIRED before clicking Signin
    }
    @Test
    public void validateSigninLink(){
        home.clickMainGetStarted();
        DbPage.clickSigninLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"),"Singinlink did not open");
    }
    @Test
    public void validateRegisterLink(){
        DbPage.clickRegister();
    }

}