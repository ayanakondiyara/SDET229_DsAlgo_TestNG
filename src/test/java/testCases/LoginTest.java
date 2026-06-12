package testCases;

import base.BaseClass;
import utilities.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.LoginPage;

public class LoginTest extends BaseClass {


        private LoginPage login;
        @BeforeMethod
        public void initPages(){
            super.setUp();
            login = new LoginPage();
        }

        @Test(dataProvider = "LoginData",dataProviderClass = TestDataProvider.class)
        public void verifyLoginTestScenarios(String username, String password,String expectedMessage) {
            login.clickSignInLink();
            login.enterCredentials(username, password);
            login.clickLoginBtn();
            Assert.assertEquals(login.getErrorMessage(), expectedMessage);
        }
    }
