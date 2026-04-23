package testCases;

import BasePage.BaseClass;
import org.testng.annotations.Test;
import pageObjects.QueuePage;

public class QueueTest extends BaseClass {

    @Test
    public void testQueueCreation() {
        // using the driver inherited from BaseClass
        QueuePage graphPage = new QueuePage(getDriver());

        // eg
        graphPage.clickOnQueueLink();
    }
}