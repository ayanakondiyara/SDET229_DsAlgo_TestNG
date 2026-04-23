package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.GraphPage;

public class GraphTest extends BaseClass {

    @Test
    public void testGraphCreation() {
        // using the driver inherited from BaseClass
        GraphPage graphPage = new GraphPage(getDriver());

        // eg
        graphPage.clickOnGraphLink();
    }
}