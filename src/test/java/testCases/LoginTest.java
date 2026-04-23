package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.LoginPage;

public class LoginTest extends BaseClass {

    @Test
    public void testLoginCreation() {
        // using the driver inherited from BaseClass
        LoginPage graphPage = new LoginPage(getDriver());

        // eg
        graphPage.clickOnGraphLink();
    }
}