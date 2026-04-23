package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.LinkedListPage;

public class LinkedListTest extends BaseClass {

    @Test
    public void testLinkedListCreation() {
        // using the driver inherited from BaseClass
        LinkedListPage graphPage = new LinkedListPage(getDriver());

        // eg
        graphPage.clickOnGLinkedListLink();
    }
}