package testCases;

import Utilities.ConfigReader;
import Utilities.DataProviderUtil;
import Utilities.LoggerLoad;
import base.BaseClass;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.LinkedListPage;
import pageObjects.LoginPage;

import java.util.Map;

public class LinkedListTest extends BaseClass {

    LinkedListPage linkedListPage;
    LoginPage loginPage;

    // =====================================================
    // Setup
    // =====================================================

    @BeforeMethod
    public void setUpPage() {

        linkedListPage = new LinkedListPage();
        loginPage = new LoginPage();

        loginPage.login(
                ConfigReader.get("username"),
                ConfigReader.get("password")
        );
    }

    // =====================================================
    // Try Editor Test
    // =====================================================

    @Test(
            priority = 2,
            dataProvider = "LinkedListTryData",
            dataProviderClass = DataProviderUtil.class
    )
    public void verifyLinkedListTryEditor(
            String subModule,
            String code,
            String expectedResult) {

        LoggerLoad.info("Starting LinkedList Try Editor Test");

        System.out.println("SubModule = " + subModule);
        System.out.println("Code = " + code);
        System.out.println("Expected = " + expectedResult);

        linkedListPage.clickLinkedListLink();
        linkedListPage.clickSubmodule(subModule);
        linkedListPage.clickTryHere();
        linkedListPage.runCodeInTryEditor(code);

        String actualResult;

        try {
            Alert alert = driver.switchTo().alert();
            actualResult = alert.getText();
            alert.accept();
        } catch (Exception e) {
            actualResult = linkedListPage.getTryEditorOutput();
        }

       Assert.assertTrue(
                actualResult.contains(expectedResult),
               "Try Editor output mismatch. Expected: " + expectedResult +
                       " but got: " + actualResult
        );
    }

    // =====================================================
    // Practice Questions Test
    // =====================================================

    @Test(
            priority = 1,
            dataProvider = "LinkedListPracticeData",
            dataProviderClass = DataProviderUtil.class
    )
    public void verifyLinkedListPracticeQuestions(
            String subModule,
            String expectedUrl) {

        LoggerLoad.info("Starting LinkedList Practice Question Test");

        linkedListPage.clickLinkedListLink();
        linkedListPage.clickSubmodule(subModule);
        linkedListPage.clickPracticeQuestions();

        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(actualUrl, expectedUrl);
    }
}