package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.StackPage;

public class StackTest extends BaseClass {

    @Test
    public void testStackCreation() {
        // using the driver inherited from BaseClass
        StackPage graphPage = new StackPage(getDriver());

        // eg
        graphPage.clickOnStackLink();
    }
}