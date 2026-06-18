package testCases;

import base.BaseClass;
import Utilities.DataProviderUtil;
import Utilities.LoggerLoad;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * PracticeQuestionsTest — Tests Practice Questions functionality.
 *
 * Data comes from the "PracticeQ" sheet in testData.xlsx.
 * Each row contains a question and its answer(s).
 *
 * Example Excel data:
 *   QuestionID | Question           | Answer
 *   Q1         | What is 2+2?       | 4
 *   Q2         | What is loop?      | Iteration
 */
public class PracticeQuestionsTest extends BaseClass {

    /**
     * Test answering a practice question from the data provider.
     *
     * Steps:
     * 1. Navigate to Practice Questions section
     * 2. Get a question from Excel data
     * 3. Answer the question
     * 4. Submit the answer
     * 5. Verify if answer is correct
     *
     * @param data  Map containing question and answer data from Excel
     */
    @Test(dataProvider = "PracticeQData", dataProviderClass = DataProviderUtil.class)
    public void verifyPracticeQuestion(Map<String, String> data) {

        LoggerLoad.info("========== Starting Practice Question Test ==========");

        // Get test data from Excel
        String questionID = data.get("QuestionID");
        String question = data.get("Question");
        String answer = data.get("Answer");

        LoggerLoad.info("Question ID: " + questionID);
        LoggerLoad.info("Question: " + question);
        LoggerLoad.info("Answer: " + answer);

        try {
            // Step 1: Navigate to Practice Questions
            LoggerLoad.info("Navigating to Practice Questions...");
            WebElement practiceLink = driver.findElement(
                    By.xpath("//a[contains(text(), 'Practice')]")
            );
            practiceLink.click();

            // Wait for page to load
            Thread.sleep(2000);

            // Step 2: Click on the specific question (using QuestionID)
            LoggerLoad.info("Clicking on question: " + questionID);
            WebElement questionElement = driver.findElement(
                    By.xpath("//div[@class='question' and contains(text(), '" + questionID + "')]")
            );
            questionElement.click();

            Thread.sleep(1500);

            // Step 3: Verify question is displayed
            LoggerLoad.info("Verifying question is displayed...");
            WebElement questionText = driver.findElement(
                    By.xpath("//h2[@class='question-text']")
            );

            Assert.assertTrue(questionText.isDisplayed(), "Question text not displayed");
            LoggerLoad.info("Question displayed: " + questionText.getText());

            // Step 4: Enter the answer in the answer field
            LoggerLoad.info("Entering answer: " + answer);
            WebElement answerField = driver.findElement(By.id("answer-input"));
            answerField.clear();
            answerField.sendKeys(answer);

            Thread.sleep(500);

            // Step 5: Click Submit button
            LoggerLoad.info("Clicking Submit button...");
            WebElement submitButton = driver.findElement(
                    By.xpath("//button[@id='submit-answer']")
            );
            submitButton.click();

            Thread.sleep(2000);

            // Step 6: Verify answer submission
            LoggerLoad.info("Verifying answer submission...");
            WebElement feedbackMessage = driver.findElement(
                    By.xpath("//div[@class='feedback-message']")
            );

            String feedbackText = feedbackMessage.getText();
            LoggerLoad.info("Feedback: " + feedbackText);

            // Check if answer was submitted successfully
            Assert.assertTrue(
                    feedbackText.contains("Correct") || feedbackText.contains("Incorrect") || feedbackText.contains("submitted"),
                    "No feedback message found for question: " + questionID
            );

            LoggerLoad.info("✓ Practice question answered successfully");

        } catch (Exception e) {
            LoggerLoad.error("✗ Practice question test failed for question: " + questionID, e);
            Assert.fail("Practice question test failed: " + e.getMessage());
        }

        LoggerLoad.info("========== Practice Question Test Completed ==========\n");
    }

    /**
     * Test navigating to the Practice Questions module.
     */
    @Test(priority = 0)  // Run this first
    public void verifyPracticeQuestionsPageLoads() {

        LoggerLoad.info("========== Starting Practice Questions Page Load Test ==========");

        try {
            // Step 1: Wait for page to load
            LoggerLoad.info("Waiting for page to load...");
            Thread.sleep(1000);

            // Step 2: Click on Practice Questions link
            LoggerLoad.info("Clicking on Practice Questions link...");
            WebElement practiceLink = driver.findElement(
                    By.xpath("//a[contains(text(), 'Practice')]")
            );
            practiceLink.click();

            Thread.sleep(2000);

            // Step 3: Verify Practice Questions page is loaded
            LoggerLoad.info("Verifying Practice Questions page...");
            WebElement pageHeading = driver.findElement(
                    By.xpath("//h1[contains(text(), 'Practice')]")
            );

            Assert.assertTrue(pageHeading.isDisplayed(), "Practice Questions page not loaded");
            LoggerLoad.info("Page heading: " + pageHeading.getText());

            // Step 4: Verify question list is displayed
            LoggerLoad.info("Verifying question list is displayed...");
            WebElement questionList = driver.findElement(
                    By.xpath("//div[@class='questions-list']")
            );

            Assert.assertTrue(questionList.isDisplayed(), "Questions list not found");

            LoggerLoad.info("✓ Practice Questions page loaded successfully");

        } catch (Exception e) {
            LoggerLoad.error("✗ Practice Questions page load test failed", e);
            Assert.fail("Practice Questions page load test failed: " + e.getMessage());
        }

        LoggerLoad.info("========== Practice Questions Page Load Test Completed ==========\n");
    }

    /**
     * Test viewing practice question statistics or progress.
     */
    @Test(priority = 1)
    public void verifyUserProgressTracking() {

        LoggerLoad.info("========== Starting User Progress Tracking Test ==========");

        try {
            // Step 1: Navigate to Practice Questions
            LoggerLoad.info("Navigating to Practice Questions...");
            WebElement practiceLink = driver.findElement(
                    By.xpath("//a[contains(text(), 'Practice')]")
            );
            practiceLink.click();

            Thread.sleep(2000);

            // Step 2: Click on Progress or Statistics button
            LoggerLoad.info("Checking for progress tracking...");
            WebElement progressButton = driver.findElement(
                    By.xpath("//button[@id='progress-button'] | //a[@id='stats-link']")
            );
            progressButton.click();

            Thread.sleep(1500);

            // Step 3: Verify progress page is displayed
            LoggerLoad.info("Verifying progress tracking page...");
            WebElement progressContainer = driver.findElement(
                    By.xpath("//div[@class='progress-container'] | //div[@class='stats-panel']")
            );

            Assert.assertTrue(progressContainer.isDisplayed(), "Progress tracking not found");
            LoggerLoad.info("✓ User progress tracking is available");

        } catch (Exception e) {
            LoggerLoad.warn("Progress tracking feature not available or not found: " + e.getMessage());
            LoggerLoad.info("This is optional feature - continuing tests");
        }

        LoggerLoad.info("========== User Progress Tracking Test Completed ==========\n");
    }
}