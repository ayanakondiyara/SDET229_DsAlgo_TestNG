package testCases;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.HomePage;

public class HomeTest extends BaseClass {

        private HomePage home;

        @BeforeMethod
        public void setUpPages() {
            super.setUp();
            home = new HomePage();
            home.clickGetStartedBtn();
            System.out.println("GetStarted Btn Clicked");
        }

    @Test
    public void validateSingInLink(){
        home.clickSignInLink();
        home.waitForTitle("Login", 10);                 // FIXED
        Assert.assertEquals(driver.getTitle(), "Login");   // FIXED
        System.out.println("SignIn Link Clicked");
    }
    @Test
    public void validateRegisterLink(){
        home.clickRegisterLink();
        home.waitForTitle("Registration",10);
        Assert.assertEquals(driver.getTitle(),"Registration");
        System.out.println("Register Link Clicked");
    }


}




