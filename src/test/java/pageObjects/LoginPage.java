package pageObjects;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage — Page Object for the login screen.
 *
 * WHY Page Objects exist (the core idea):
 *   Without Page Objects, your test would look like:
 *
 *       driver.findElement(By.id("id_username")).sendKeys("user@email.com");
 *       driver.findElement(By.id("id_password")).sendKeys("password");
 *       driver.findElement(By.xpath("//input[@type='submit']")).click();
 *
 *   The problem: if the id changes from "id_username" to "username", you
 *   have to hunt through every test that used it. With Page Objects, you
 *   change ONE line in LoginPage and every test is fixed automatically.
 *
 * HOW @FindBy works:
 *   @FindBy(id = "id_username")
 *   WebElement usernameField;
 *
 *   This tells Selenium: "When I use 'usernameField', find the element
 *   on the page whose HTML id attribute equals 'id_username'."
 *   PageFactory (called in BasePage constructor) sets this up lazily —
 *   the actual browser lookup happens the first time you call a method
 *   on the element (like .sendKeys() or .click()).
 *
 *     WHAT IS A PAGE OBJECT?
 *    A Page Object is a Java class that represents ONE screen (or part of a
 *    screen) in your web application. It holds:
 *       - The element locators  (@FindBy fields)
 *       - The user actions      (methods like login(), clickSignIn())
 *
 *     Tests NEVER call Selenium directly (driver.findElement, .click(), etc.).
 *     Tests only call the methods in this class. This means:
 *       • If a locator changes, you fix ONE line here — not every test.
 *       • Tests read like plain English: loginPage.login(user, pass)
 *       • Selenium details are hidden from the test, making tests cleaner.
 *
 * LOCATOR STRATEGIES (choose the most stable one):
 *   id       → fastest & most reliable when the element has a unique id
 *   name     → good for form inputs with a 'name' attribute
 *   linkText → for <a> tags with exact visible text
 *   xpath    → powerful but fragile; use only when id/name/linkText won't work
 *   cssSelector → a cleaner alternative to xpath for most situations
 *    HOW TO FIND THE RIGHT LOCATOR YOURSELF (important skill):
 *    1. Open the site in Chrome.
 *    2. Right-click the element you want → click "Inspect".
 *    3. Look at the HTML tag:
 *       <a href="...">Sign In</a>          → use linkText = "Sign In"
 *       <input id="id_username" ...>       → use id = "id_username"
 *       <button class="btn btn-primary">   → use cssSelector = ".btn.btn-primary"
 *    4. Copy the most unique, stable attribute — prefer id > linkText > css > xpath.
 *
 */
public class LoginPage extends BasePage {

    // ─── ELEMENT LOCATORS ────────────────────────────────────────────────────
    // Each field represents ONE element on the page.
    // @FindBy tells Selenium how to find it.
    // These are 'package-private' (no modifier) — visible within the package.

    @FindBy(className = "btn")
    WebElement getStartedBtn;

    @FindBy(linkText = "Sign in")
    WebElement signInLink;

    @FindBy(id = "id_username")          // matches: <input id="id_username" ...>
    WebElement usernameField;

    @FindBy(id = "id_password")          // matches: <input id="id_password" ...>
    WebElement passwordField;

    @FindBy(css = "input[type='submit']")// matches: <input type="submit" ...>
    WebElement loginSubmitButton;

    // Error message shown when login fails
    @FindBy(className = "alert-primary")  // matches: <div class="alert-danger">...</div>
            WebElement errorMessage;

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────

    /**
     * Constructor — called whenever a test does: new LoginPage(driver)
     *
     * MUST call super(driver). Here is exactly what that does:
     *   1. Stores 'driver' in BasePage so all methods here can use it.
     *   2. Calls PageFactory.initElements(driver, this):
     *        - Scans THIS class for all @FindBy annotations.
     *        - Wraps each WebElement field in a Proxy object.
     *        - The Proxy calls driver.findElement() automatically the
     *          first time you use the field (lazy evaluation).
     *
     * WITHOUT super(driver):
     *   All @FindBy fields stay as null. The first call to .click() or
     *   .sendKeys() on any of them would throw a NullPointerException.
     *
     * @param driver  the WebDriver instance created by DriverFactory and
     *                stored in BaseClass. Passed in from the test.
     */
    public LoginPage() {
        super(); // REQUIRED — initialises all @FindBy fields above

    }

    // ─── ACTION METHODS ───────────────────────────────────────────────────────
    // Methods represent USER ACTIONS on this page.
    // Tests call these methods — they don't call Selenium directly.
    // Each method represents ONE user action.
    // Keep methods small and focused — one action per method.
    // The combined login() method at the end chains them together.

    /**
     * Clicks the "Get Started" button on the Home page.
     *
     * WHY waitForElementToClick() instead of plain .click()?
     *   waitForElementToClick() (defined in BasePage) does THREE things:
     *     1. Scrolls the element into view (fixes "element not interactable" errors).
     *     2. Waits up to 10 seconds for it to become clickable.
     *     3. Clicks it.
     *   A plain .click() skips steps 1 and 2 — it fails if the element is
     *   off-screen or if the page hasn't finished loading yet.
     */
    public void clickGetStarted() {
        waitForElementToClick(getStartedBtn,10);
    }

    /**
     * Clicks the "Sign In" hyperlink after the Get Started page loads.
     *
     * This is a navigation link that takes the user from the features/landing
     * page to the actual login form (where username/password fields appear).
     *
     * NOTE: This element only exists AFTER clickGetStarted() has been called
     * and the page has loaded. Calling this method before clickGetStarted()
     * will throw NoSuchElementException.
     */
    public void clickSignIn() {
        waitForElementToClick(signInLink, 10);
    }

    /**
     * Types a username into the username field.
     *
     * sendKeys() simulates keyboard input, typing one character at a time.
     * clear() is called first to remove any text already in the field.
     * WHY? If a test runs twice without clearing, the second run appends
     * to whatever was already typed — producing "user@email.comuser@email.com".
     *
     * sendKeys() simulates keyboard input character by character.
     *
     * @param username  the username string to type into the field
     */
    public void enterUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    /**
     * Types a password into the password field.
     *
     * Same clear() + sendKeys() pattern as enterUsername().
     *
     * @param password  the password string to type into the field
     */
    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void navigateToLoginPage() {
        waitForElementToClick(getStartedBtn,10);
    }


    /**
     * Clicks the login/submit button.
     *
     * We use waitForElementToClick() from BasePage instead of a plain .click()
     * because the page might still be loading when we reach this line.
     * waitForElementToClick() scrolls it into view AND waits until it is
     * enabled before clicking — much safer.
     */


    public void clickLoginButton() {
        waitForElementToClick(loginSubmitButton, 10);
    }

    /**
     * Performs the COMPLETE login journey in one single call.
     *
     * Full flow:
     *   Home page
     *     → clickGetStarted()   → Features / landing page
     *     → clickSignIn()       → Login form page
     *     → enterUsername()     → types username into the field
     *     → enterPassword()     → types password into the field
     *     → clickLoginButton()  → submits the form
     *
     * WHY combine into one method?
     *   Every test that needs to be logged in calls the same 5 steps.
     *   Without this method, you would repeat those 5 lines in every
     *   single test — and if step 2 changes, you update it in 10 places.
     *   With login(), you update it in ONE place and all tests benefit.
     *
     * USAGE IN TESTS (clean and readable):
     *   LoginPage loginPage = new LoginPage(driver);
     *   loginPage.login(ConfigReader.get("Username"), ConfigReader.get("Password"));
     *
     * @param username  username to log in with (read from config.properties)
     * @param password  password to log in with (read from config.properties)
     */
    public void login(String username, String password) {
        navigateToLoginPage();
        clickSignIn();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Returns the error message text shown after a failed login attempt.
     *
     * The error div only appears AFTER a failed submit — it does not exist
     * on page load. waitForElementToBeDisplayed() polls every 500ms until
     * the element becomes visible, up to 10 seconds. Without this wait,
     * getText() might return an empty string because the DOM update hasn't
     * happened yet when Selenium reads it.
     *
     * USAGE IN TESTS:
     *   String error = loginPage.getErrorMessage();
     *   Assert.assertFalse(error.isEmpty(), "Expected an error message.");
     *
     * @return  the visible error message text, e.g. "Invalid credentials."
     */
    public String getErrorMessage() {
        waitForElementToDisplayed(errorMessage, 10);
        return errorMessage.getText();
    }

    /**
     * Returns true if the user successfully logged in.
     *
     * HOW IT WORKS:
     *   After a successful login, the application redirects to the home/
     *   dashboard page. waitForUrl() waits up to 10 seconds for the browser
     *   URL to match the expected post-login URL.
     *     - If the URL matches within 10 seconds → return true  (login OK)
     *     - If the URL never matches (timeout)   → catch block → return false
     *
     * ⚠️ IMPORTANT: verify this URL is correct for your application.
     *   Open the site, log in manually, and copy the URL from the address bar.
     *   Paste it here to replace the current value if it differs.
     *
     * @return  true if on the expected post-login page, false otherwise
     */
    public boolean isLoginSuccessful() {
        try {
            // Wait up to 10 seconds for the URL to change away from the login page.
            // If it times out, the catch block returns false.
            waitForUrl("https://dsportalapp.herokuapp.com/home", 10);
            return true;
        } catch (Exception e) {
            // waitForUrl throws TimeoutException if the URL doesn't match.
            // We catch it and return false rather than crashing the test —
            // the test's Assert.assertTrue() will then fail with a clear message.
            return false;
        }
    }
}