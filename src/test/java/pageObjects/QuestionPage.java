package pageObjects;

import base.BasePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * QuestionPage — Page Object for individual practice question pages.
 * URLs: https://dsportalapp.herokuapp.com/question/1
 *       https://dsportalapp.herokuapp.com/question/2  etc.
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WHAT THIS PAGE LOOKS LIKE (from Images 6 & 7):
 *
 *  ┌──────────────────────┬──────────────────────────────────┐
 *  │ QUESTION             │  1 def search(input_list, num):  │
 *  │                      │  2                               │
 *  │ Write a Python       │  (CodeMirror editor)             │
 *  │ Program to check     │                                  │
 *  │ if an element is...  │  [  Run  ]  [  Submit  ]         │
 *  │                      │                                  │
 *  │ Input: [12,23,45,    │  (result panel)                  │
 *  │ 67,6,90], 12         │  "Error occurred during          │
 *  │ Output: Element Found│   submission"                    │
 *  └──────────────────────┴──────────────────────────────────┘
 *
 * TWO BUTTONS AND THEIR BEHAVIOURS:
 *
 *  [Run] button:
 *    → Valid code:   output appears in the result panel below
 *    → Invalid code: browser alert popup appears (like TryEditorPage)
 *                    alert shows error e.g. "SyntaxError: bad input on line 2"
 *                    user must click OK to dismiss
 *
 *  [Submit] button:
 *    → Correct solution:  result panel shows success message
 *    → Wrong solution:    result panel shows "Error occurred during submission"
 *    Note: Submit does NOT show a browser alert — it updates the result panel.
 *          This is DIFFERENT from Run. Run uses alert; Submit uses page element.
 *
 * PRE-FILLED EDITOR:
 *   The editor already has a function signature when the page loads:
 *     def search(input_list, num):
 *   When writing test code, you must either:
 *     a) ADD to the existing signature (append code after it), OR
 *     b) REPLACE the entire editor content using enterCodeInEditor()
 *   enterCodeInEditor() (from BasePage) replaces ALL content — use it.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class QuestionPage extends BasePage {

    // ═══════════════════════════════════════════════════════════════════════
    // ELEMENT LOCATORS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * The question text area on the LEFT side of the page.
     *
     * Used to verify the correct question was loaded by reading its heading.
     * xpath: any <h2> or heading element inside the question panel.
     * Adjust this xpath if the actual HTML uses a different tag.
     */
    @FindBy(xpath = "//div[@class='question-content']//h2 | //h4[text()='QUESTION']")
    WebElement questionHeading;

    /**
     * The "Run" button — runs the code and shows output or an alert.
     *
     * This page has TWO buttons (Run and Submit). We use xpath with text()
     * to specifically target the "Run" button and not accidentally click Submit.
     *
     * xpath = "//button[text()='Run']"
     *   Finds a <button> whose inner text is exactly "Run".
     */
    @FindBy(xpath = "//button[text()='Run']")
    WebElement runButton;

    /**
     * The "Submit" button — submits the solution for evaluation.
     *
     * IMPORTANT: Clicking Submit sends the code to the server for grading.
     * The result appears in the result panel BELOW the buttons (not as an alert).
     */
    @FindBy(xpath = "//button[text()='Submit']")
    WebElement submitButton;

    /**
     * The result panel that shows submission outcome.
     *
     * After clicking Submit:
     *   Correct solution  → shows something like "Correct!" or success text
     *   Wrong solution    → shows "Error occurred during submission"
     *
     * ALSO used after clicking Run for valid code — shows the run output.
     *
     * id = "output" is the same element used in TryEditorPage.
     * The question pages share the same output element id.
     */
    @FindBy(id = "output")
    WebElement resultPanel;

    // ═══════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════════

    public QuestionPage(WebDriver driver) {
        super();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACTION METHODS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Types Python code into the editor, then clicks Run.
     * Use for VALID code — code you expect to run without errors.
     *
     * NOTE: enterCodeInEditor() REPLACES the pre-filled function signature.
     * You must include the full function definition in your code string.
     *
     * Example:
     *   questionPage.runCode(
     *       "def search(input_list, num):\n" +
     *       "    if num in input_list:\n" +
     *       "        return 'Element Found'\n" +
     *       "    else:\n" +
     *       "        return 'Not Found'"
     *   );
     *
     * @param code  complete Python code to enter and run
     */
    public void runCode(String code) {
        enterCodeInEditor(code);
        waitForElementToClick(runButton, 10);
    }

    /**
     * Types INVALID Python code, clicks Run, waits for the alert popup,
     * captures the error message, then dismisses the alert.
     *
     * FLOW (from Image 4 — same behaviour as TryEditorPage):
     *   1. Code is injected into editor.
     *   2. Run is clicked.
     *   3. Browser alert appears with error text.
     *   4. We capture the text.
     *   5. We click OK (accept) to dismiss.
     *
     * @param code  intentionally broken Python code
     * @return      the alert error message text
     */
    public String runInvalidCodeAndGetAlert(String code) {
        enterCodeInEditor(code);
        waitForElementToClick(runButton, 10);

        Alert alert = getAlert(10); // from BasePage — waits for alert

        if (alert == null) {
            throw new RuntimeException(
                    "[QuestionPage] Expected an error alert but none appeared."
            );
        }

        String message = alert.getText();
        alert.accept(); // click OK to dismiss
        return message;
    }

    /**
     * Types code into the editor, then clicks Submit to send for grading.
     * After submission, the result panel is updated with the outcome.
     *
     * IMPORTANT:
     *   Submit sends code to the server for grading — different from Run.
     *   Run just executes locally; Submit compares output against test cases.
     *
     * @param code  the complete Python solution to submit
     */
    public void submitCode(String code) {
        enterCodeInEditor(code);
        waitForElementToClick(submitButton, 10);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // QUERY METHODS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Returns the text currently shown in the result panel.
     *
     * Use after runCode() for valid code:
     *   String output = questionPage.getResultPanelText();
     *   Assert.assertTrue(output.contains("Element Found"), ...);
     *
     * Use after submitCode():
     *   String result = questionPage.getResultPanelText();
     *   Assert.assertFalse(result.contains("Error"), "Submission should succeed");
     *
     * waitForElementToBeDisplayed() ensures the panel has content before we read it.
     *
     * @return  the result panel text, trimmed of whitespace
     */
    public String getResultPanelText() {
        waitForElementToDisplayed(resultPanel, 10);
        return resultPanel.getText().trim();
    }

    /**
     * Returns true if the submission was successful (no error in result panel).
     *
     * After clicking Submit:
     *   "Error occurred during submission" → wrong answer → return false
     *   Any other text (success)           → correct answer → return true
     *
     * @return  true if submission did NOT produce an error
     */
    public boolean isSubmissionSuccessful() {
        String result = getResultPanelText();
        // Returns false if the result contains the known error phrase.
        // !contains(...) means "true if the error phrase is NOT present".
        return !result.contains("Error occurred during submission");
    }
}