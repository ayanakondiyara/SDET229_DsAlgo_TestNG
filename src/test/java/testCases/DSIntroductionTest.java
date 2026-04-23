package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.DSIntroductionPage;

public class DSIntroductionTest extends BaseClass {

    @Test
    public void testGraphCreation() {
        // using the driver inherited from BaseClass
        DSIntroductionPage graphPage = new DSIntroductionPage(getDriver());

        // eg
        graphPage.clickOnLink();
    }
}