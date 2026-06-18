package testCases;

import Utilities.ConfigReader;
import Utilities.DataProviderUtil;
import base.BaseClass;
import pageObjects.LoginPage;
import pageObjects.ArrayPage;
import Utilities.LoggerLoad;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * ArrayTest — TestNG test class for Array module
 */
public class ArrayTest extends BaseClass {

    ArrayPage arrayPage;
    LoginPage loginPage;
    // ================= Setup =================
    @BeforeMethod
    public void setUpPage() {
        arrayPage = new ArrayPage();
        loginPage = new LoginPage();

        loginPage.login(
                ConfigReader.get("username"),
                ConfigReader.get("password")
        );
    }

    // ================= Test 1: Try Editor =================
    @Test(
            priority = 1,
            dataProvider = "ArrayTryData",
     dataProviderClass = DataProviderUtil.class
    )
    public void verifyArrayTryEditor(
            String subModule,
            String code,
            String expectedResult) {

        LoggerLoad.info("Starting Array Try Editor Test");

        arrayPage.clickArrayLink();
        arrayPage.clickSubmodule(subModule);
        arrayPage.clickTryHere();
        arrayPage.runCodeInTryEditor(code);

        String actualResult;

        try {
            Alert alert = driver.switchTo().alert();
            actualResult = alert.getText();
            alert.accept();
        } catch (Exception e) {
            actualResult = arrayPage.getTryEditorOutput();
        }

        Assert.assertTrue(
                actualResult.contains(expectedResult),
                "Try Editor output mismatch. Expected: " + expectedResult +
                        " but got: " + actualResult
        );
    }

    // ================= Test 2: Practice Questions =================

    @Test(
            priority = 2,
            dataProvider = "PracticeQData",
            dataProviderClass = DataProviderUtil.class
    )
    public void verifyPracticeQuestions(Map<String, String> data) {

        System.out.println("FULL ROW = " + data);
        System.out.println("KEYS = " + data.keySet());

        String subModule    = data.get("SubModule");
        String question     = data.get("Question");
        String code         = data.get("Code").replace("\\n", "\n");
        String runResult    = data.get("RunResult");
        String submitResult = data.get("SubmitResult");

        LoggerLoad.info("Starting Practice Questions Test");

        arrayPage.clickArrayLink();
        arrayPage.clickSubmodule(subModule);
        arrayPage.clickPracticeButton("Practice Questions");
        arrayPage.clickQuestion(question);

        arrayPage.runPracticeCode(code);

        Assert.assertTrue(
                arrayPage.getPracticeOutput().contains(runResult),
                "Run result mismatch. Expected: " + runResult
        );

        arrayPage.submitPracticeCode(code);

        Assert.assertTrue(
                arrayPage.getSubmissionMessage().contains(submitResult),
                "Submission result mismatch. Expected: " + submitResult
        );
    }
}