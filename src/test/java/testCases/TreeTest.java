package testCases;

import base.BaseClass;
import org.testng.annotations.Test;
import pageObjects.TreePage;

public class TreeTest extends BaseClass {

    @Test
    public void testTreeCreation() {
        // using the driver inherited from BaseClass
        TreePage graphPage = new TreePage(getDriver());

        // eg
        graphPage.clickOnTreeLink();
    }
}