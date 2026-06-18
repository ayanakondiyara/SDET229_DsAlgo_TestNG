package pageObjects;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * PracticeQuestionsPage — Page Object for the practice questions listing.
 * URL: https://dsportalapp.herokuapp.com/array/practice
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WHAT THIS PAGE SHOWS:
 *   A list of 4 question links:
 *     • Search the array            → /question/1
 *     • Max Consecutive Ones        → /question/2
 *     • Find Numbers with Even Digits → /question/3
 *     • Squares of a Sorted Array   → /question/4
 *
 * DESIGN — why only ONE navigation method (openQuestionByName):
 *   Previously, individual methods like openSearchTheArray(),
 *   openMaxConsecutiveOnes() were created — one method per question.
 *   That caused a bug: PracticeQuestionsTest called those methods
 *   but they were never defined here.
 *
 *   The fix: ONE dynamic method that finds ANY question by its link text.
 *   Works for current 4 questions AND any future questions added to the site.
 *   Data-driven tests pass the question name directly from Excel —
 *   no code changes needed when question names change.
 *
 * ─────────────────────────────────────────────────────────────────────────
 * COMPLETE FLOW:
 *   Login → Arrays → openArraysInPython() → clickPracticeQuestions()
 *   → [this page]
 *   → openQuestionByName("Search the array")
 *   → QuestionPage (Run + Submit)
 * ─────────────────────────────────────────────────────────────────────────
 */
public class PracticeQuestionsPage extends BasePage {

    // ═══════════════════════════════════════════════════════════════════════
    // ELEMENT LOCATORS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * ALL question links collected as a List<WebElement>.
     *
     * @FindBy with List<WebElement>:
     *   PageFactory finds EVERY element matching the locator and puts
     *   them all into this list. Size = number of questions on the page.
     *
     * LOCATOR: xpath "//div[@class='list-group']//a"
     *   Finds all <a> tags inside the list-group container.
     *
     * IF THIS LOCATOR FAILS:
     *   Open /array/practice → right-click a question → Inspect.
     *   Find the parent container div and replace 'list-group' with
     *   whatever class that div actually has on your version of the site.
     */
    @FindBy(xpath = "//div[@class='list-group']//a")
    private List<WebElement> questionLinks;

    // ═══════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════════

    public PracticeQuestionsPage(WebDriver driver) {
        super(); // BasePage stores driver + runs PageFactory
    }

    // ═══════════════════════════════════════════════════════════════════════
    // NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Clicks a question by its exact visible link text.
     *
     * LOOP LOGIC:
     *   1. Goes through every link in questionLinks.
     *   2. .trim() removes invisible whitespace that can cause mismatches.
     *   3. When text matches → click and return immediately.
     *   4. If no match found → RuntimeException with helpful message.
     *
     * USED IN HARDCODED TESTS:
     *   page.openQuestionByName("Search the array");
     *   page.openQuestionByName("Max Consecutive Ones");
     *
     * USED IN DATA-DRIVEN TESTS (from Excel "testcase" column):
     *   page.openQuestionByName(data.get("testcase"));
     *   → "testcase" cell value = "Search the array" → clicks that link
     *
     * @param questionName  exact link text (case-sensitive, matches the website)
     */
    public void openQuestionByName(String questionName) {
        for (WebElement link : questionLinks) {
            if (link.getText().trim().equals(questionName)) {
                waitForElementToClick(link, 10);
                return;
            }
        }
        // No match found — give a debugging-friendly error
        throw new RuntimeException(
                "[PracticeQuestionsPage] Question not found: '" + questionName + "'\n" +
                        "Available questions: " + questionLinks.stream()
                        .map(e -> e.getText().trim()).toList() + "\n" +
                        "Check the 'testcase' column in Excel matches the exact link text."
        );
    }

    // ═══════════════════════════════════════════════════════════════════════
    // QUERY METHODS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Returns the number of question links on the page.
     * Use to assert the page shows exactly 4 questions.
     *
     * @return  question count (e.g. 4)
     */
    public int getQuestionCount() {
        return questionLinks.size();
    }

    /**
     * Returns true if a question with the given title is visible.
     * Use to verify each specific question is present on the page.
     *
     * @param questionTitle  exact link text to look for
     * @return               true if visible, false if not found
     */
    public boolean isQuestionVisible(String questionTitle) {
        return questionLinks.stream()
                .anyMatch(link -> link.getText().trim().equals(questionTitle));
    }
}