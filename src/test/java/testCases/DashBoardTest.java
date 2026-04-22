package testCases;

import base.BaseClass;
import org.testng.annotations.Test;
import pageObjects.DashBoardPage;

public class DashBoardTest extends BaseClass {

    @Test
    public void testGraphCreation() {
        // using the driver inherited from BaseClass
        DashBoardPage graphPage = new DashBoardPage(getDriver());

        // eg
        DashBoardPage.clickOnLink();
    }
}