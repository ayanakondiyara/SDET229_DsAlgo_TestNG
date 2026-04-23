package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.ArrayPage;

public class ArrayTest extends BaseClass {

    @Test
    public void testGraphCreation() {
        // using the driver inherited from BaseClass
        ArrayPage graphPage = new ArrayPage(getDriver());

        // eg
        graphPage.clickOnGraphLink();
    }
}