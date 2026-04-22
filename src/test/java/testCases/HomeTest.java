package testCases;

import base.BaseClass;
import org.testng.annotations.Test;
import pageObjects.HomePage;

public class HomeTest extends BaseClass {

    @Test
    public void testCreation() {
        // using the driver inherited from BaseClass
        HomePage graphPage = new HomePage(getDriver());

        // eg
        graphPage.clickOnLink();
    }
}