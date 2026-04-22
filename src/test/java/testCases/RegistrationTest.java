package testCases;

import base.BaseClass;
import org.testng.annotations.Test;
import pageObjects.RegistrationPage;

public class RegistrationTest extends BaseClass {

    @Test
    public void testGraphCreation() {
        // using the driver inherited from BaseClass
        RegistrationPage graphPage = new RegistrationPage(getDriver());

        // eg
        graphPage.clickOnRegistrationLink();
    }
}