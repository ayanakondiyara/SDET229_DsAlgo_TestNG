package pageObjects;

import base.BasePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TryEditorPage — Page Object for the Try Editor.
 * URL: https://dsportalapp.herokuapp.com/tryEditor
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WHAT THIS PAGE LOOKS LIKE (from Image 3 & 4):
 *   ┌─────────────────────────────────┐
 *   │  1 |                            │  ← CodeMirror editor (line numbers on left)
 *   │  2 |                            │
 *   │    |                            │
 *   └─────────────────────────────────┘
 *   [  Run  ]
 *   ─────────────────────────────────
 *   (output appears here after Run)
 *
 * TWO BEHAVIOURS WHEN YOU CLICK RUN (from Image 4):
 *   1. Valid code   → output appears in the panel below the editor.
 *   2. Invalid code → a BROWSER ALERT POPUP appears with the error message.
 *                     The user must click OK to dismiss it.
 *
 * KEY CONCEPT — Browser Alert vs. Page Element:
 *   A browser alert is NOT a <div> on the page. It is a native OS dialog
 *   that the browser creates. Selenium cannot use findElement() to interact
 *   with it. Instead, you use: driver.switchTo().alert()
 *
 *   This "switches" Selenium's focus from the page to the alert dialog,
 *   letting you read its text (alert.getText()) or dismiss it (alert.accept()).
 *
 * WHY IS THIS A SEPARATE CLASS FROM ArrayPage?
 *   The Try Editor is reached from MULTIPLE places:
 *     - From /array/arrays-in-python/    → clicks "Try here>>>"
 *     - From /array/arrays-using-list/   → clicks "Try here>>>"
 *     - From /array/basic-operations...  → clicks "Try here>>>"
 *     - From /array/applications-of-...  → clicks "Try here>>>"
 *   All of them land on the SAME /tryEditor URL with the same editor.
 *   One TryEditorPage class serves all four sub-modules.
 *   This follows the DRY principle (Don't Repeat Yourself).
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class TryEditorPage extends BasePage {

    // ═══════════════════════════════════════════════════════════════════════
    // ELEMENT LOCATORS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * The "Run" button below the code editor.
     *
     * xpath = "//button[text()='Run']"
     *   Finds any <button> element whose text content is exactly "Run".
     *   We use xpath here because the button has no id or unique class.
     *   text()='Run' is more specific than className, which could match
     *   multiple elements if other buttons share the same class.
     */
    @FindBy(xpath = "//button[text()='Run']")
    WebElement runButton;

    /**
     * The output panel that displays results after running code.
     *
     * id = "output" is the most reliable locator — ids are unique per page.
     * This panel:
     *   - Is empty when the page first loads.
     *   - Shows the code output after successful execution.
     *   - Stays empty (or shows previous output) when invalid code causes an alert.
     */
    @FindBy(id = "output")
    WebElement outputPanel;

    // ═══════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════════

    public TryEditorPage(WebDriver driver) {
        super(); // activates @FindBy fields
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACTION METHODS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Types Python code into the CodeMirror editor, then clicks Run.
     * Use this for VALID code that you expect to produce output.
     *
     * HOW enterCodeInEditor() works (defined in BasePage):
     *   CodeMirror renders a custom UI over a hidden <textarea>.
     *   Selenium's sendKeys() cannot type into a hidden textarea.
     *   enterCodeInEditor() uses JavaScript to call CodeMirror's own
     *   setValue() API, which injects the code directly into the editor.
     *
     * MULTI-LINE CODE:
     *   Use \n to separate lines in the string:
     *     runCode("arr = [1, 2, 3]\nprint(arr[0])")
     *   This puts "arr = [1, 2, 3]" on line 1 and "print(arr[0])" on line 2.
     *
     * @param code  the Python code to type and run
     */
    public void runCode(String code) {
        enterCodeInEditor(code);                 // inject code via JavaScript
        waitForElementToClick(runButton, 10);   // click the Run button
    }

    /**
     * Returns the text shown in the output panel after running code.
     *
     * waitForElementToBeDisplayed() ensures the output has appeared before
     * we read it. Without this wait, getText() might return an empty string
     * because the server hasn't responded yet.
     *
     * .trim() removes any leading/trailing whitespace the editor might add,
     * preventing false failures from invisible characters.
     *
     * @return  the output text, e.g. "Hello Array" or "5"
     */
    public String getOutput() {
        waitForElementToDisplayed(outputPanel, 10);
        return outputPanel.getText().trim();
    }

    /**
     * Runs code that is EXPECTED to cause an error, then handles the alert.
     *
     * FLOW (from Image 4):
     *   1. enterCodeInEditor() injects the broken code.
     *   2. runButton click triggers Python execution on the server.
     *   3. Server detects a syntax/runtime error.
     *   4. Page shows a browser ALERT with the error message.
     *   5. We switch to the alert, capture the message, then click OK.
     *
     * WHY RETURN THE ALERT MESSAGE?
     *   Tests can then assert the message contains the expected error type:
     *   Assert.assertTrue(msg.contains("SyntaxError"), "Expected a SyntaxError");
     *
     * @param code  the intentionally broken Python code
     * @return      the alert message text (e.g. "SyntaxError: bad input on line 2")
     * @throws RuntimeException if no alert appears within 10 seconds
     */
    public String runInvalidCodeAndGetAlertMessage(String code) {
        enterCodeInEditor(code);
        waitForElementToClick(runButton, 10);

        // getAlert(10) is defined in BasePage.
        // It waits up to 10 seconds for an alert to appear, then returns it.
        // driver.switchTo().alert() moves Selenium's focus to the dialog.
        Alert alert = getAlert(10);

        if (alert == null) {
            throw new RuntimeException(
                    "[TryEditorPage] Expected an error alert but none appeared. " +
                            "Code may have run without error: " + code
            );
        }

        // Read the alert message BEFORE dismissing it.
        // Once accept() is called, the alert is gone — you cannot read it anymore.
        String alertMessage = alert.getText();

        // accept() clicks the OK button on the alert dialog.
        // This dismisses the popup and returns focus to the page.
        alert.accept();

        return alertMessage;
    }

    /**
     * Dismisses an alert if one is currently present on the screen.
     *
     * WHY is this needed?
     *   If a previous test left an alert open (due to a test failure mid-way),
     *   the next interaction with the page will throw UnhandledAlertException.
     *   This cleanup method can be called in @AfterMethod if needed.
     */
    public void dismissAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept(); // click OK to dismiss
        } catch (Exception e) {
            // No alert present — nothing to do. This is expected in normal flow.
        }
    }
}