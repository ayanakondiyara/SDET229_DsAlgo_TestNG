package testCases;

import base.BaseClass;
import base.BasePage;
import Utilities.DataProviderUtil;
import Utilities.LoggerLoad;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * TryEditorTest — Tests the Python Code Editor functionality.
 *
 * Data comes from the "PythonCode" sheet in testData.xlsx.
 * Each row contains a Python code snippet and its expected output.
 *
 * Example Excel data:
 *   Code              | ExpectedOutput | IsInvalidCode | TestScenario
 *   print('Hello')    | Hello          | false         | basic print
 *   x = 10            | (no output)    | false         | variable assignment
 *   print(invalid)    | (error)        | true          | undefined variable
 */
public class TryEditorTest extends BaseClass {

    /**
     * Test running Python code in the Try Editor.
     *
     * Steps:
     * 1. Navigate to the Try Editor
     * 2. Enter Python code from Excel data
     * 3. Click Run button
     * 4. Verify the output matches expected result
     *
     * @param data  Map containing: {"Code": "...", "ExpectedOutput": "...", "IsInvalidCode": "..."}
     */
    @Test(dataProvider = "pythonCodeData", dataProviderClass = DataProviderUtil.class)
    public void verifyPythonCodeExecution(Map<String, String> data) {

        LoggerLoad.info("========== Starting Try Editor Test ==========");

        // Get test data from Excel
        String code = data.get("Code").replace("\\n", "\n");  // Handle newlines
        String expectedOutput = data.get("ExpectedOutput");
        String isInvalidCode = data.get("IsInvalidCode");
        String testScenario = data.get("TestScenario");

        LoggerLoad.info("Test Scenario: " + testScenario);
        LoggerLoad.info("Code to execute: " + code);
        LoggerLoad.info("Expected Output: " + expectedOutput);
        LoggerLoad.info("Is Invalid Code: " + isInvalidCode);

        try {
            // Step 1: Navigate to Try Editor
            LoggerLoad.info("Navigating to Try Editor...");
            WebElement tryEditorLink = driver.findElement(
                    By.xpath("//a[contains(text(), 'Try Editor')]")
            );
            tryEditorLink.click();

            // Wait for page to load
            Thread.sleep(2000);

            // Step 2: Clear the editor (if it has default content)
            LoggerLoad.info("Clearing editor...");
            BasePage basePage = new BasePage();
            basePage.enterCodeInEditor("");
            Thread.sleep(500);

            // Step 3: Enter the Python code into the CodeMirror editor
            LoggerLoad.info("Entering Python code into editor...");
            basePage.enterCodeInEditor(code);
            Thread.sleep(1000);

            // Step 4: Click the Run button
            LoggerLoad.info("Clicking Run button...");
            WebElement runButton = driver.findElement(By.id("run-button"));
            runButton.click();

            // Wait for code execution
            Thread.sleep(2000);

            // Step 5: Get the actual output
            LoggerLoad.info("Retrieving output...");
            WebElement outputWindow = driver.findElement(By.id("output"));
            String actualOutput = outputWindow.getText().trim();

            LoggerLoad.info("Actual Output: " + actualOutput);

            // Step 6: Verify the output
            if (isInvalidCode.equalsIgnoreCase("true")) {
                // For invalid code, we expect an error message
                LoggerLoad.info("Verifying error handling for invalid code...");
                Assert.assertTrue(
                        actualOutput.contains("Error") || actualOutput.contains("Exception"),
                        "Expected error message for invalid code, but got: " + actualOutput
                );
                LoggerLoad.info("✓ Invalid code error handled correctly");

            } else {
                // For valid code, output should match expected output
                LoggerLoad.info("Verifying correct output for valid code...");
                Assert.assertEquals(
                        actualOutput,
                        expectedOutput,
                        "Output does not match expected result. Expected: " + expectedOutput + ", Got: " + actualOutput
                );
                LoggerLoad.info("✓ Code executed successfully with correct output");
            }

        } catch (Exception e) {
            LoggerLoad.error("✗ Try Editor test failed for scenario: " + testScenario, e);
            Assert.fail("Try Editor test failed: " + e.getMessage());
        }

        LoggerLoad.info("========== Try Editor Test Completed ==========\n");
    }

    /**
     * Test that code editor is accessible.
     */
    @Test(priority = 0)  // Run this first
    public void verifyTryEditorPageLoads() {

        LoggerLoad.info("========== Starting Try Editor Load Test ==========");

        try {
            // Step 1: Click on Try Editor
            LoggerLoad.info("Clicking on Try Editor link...");
            WebElement tryEditorLink = driver.findElement(
                    By.xpath("//a[contains(text(), 'Try Editor')]")
            );
            tryEditorLink.click();

            // Wait for page to load
            Thread.sleep(2000);

            // Step 2: Verify editor is displayed
            LoggerLoad.info("Verifying editor is loaded...");
            WebElement codeEditor = driver.findElement(By.className("CodeMirror"));

            Assert.assertTrue(codeEditor.isDisplayed(), "Code editor not found");

            LoggerLoad.info("✓ Try Editor page loaded successfully");

        } catch (Exception e) {
            LoggerLoad.error("✗ Try Editor page load test failed", e);
            Assert.fail("Try Editor page load test failed: " + e.getMessage());
        }

        LoggerLoad.info("========== Try Editor Load Test Completed ==========\n");
    }
}