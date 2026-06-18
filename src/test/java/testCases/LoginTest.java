package testCases;
import Utilities.ConfigReader;
import Utilities.DataProviderUtil;
import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.LoginPage;

import java.util.Map;

/**
 * LoginTest — test class for all login-related test scenarios.
 *
 * EXTENDS BaseClass:
 *   This gives LoginTest the @BeforeMethod (open browser) and
 *   @AfterMethod (close browser) automatically. LoginTest itself
 *   only needs to contain the actual @Test methods.
 *
 * 'driver' field:
 *   Inherited from BaseClass. It is available here because
 *   BaseClass declares it as 'protected WebDriver driver;'.
 *   We pass it into page objects: new LoginPage(driver).
 *
 * TEST NAMING CONVENTION:
 *   Method names describe what is being tested and what the
 *   expected outcome is. Good: verifyLoginWithValidCredentials()
 *   Bad: test1(), testLogin()
 */
public class LoginTest extends BaseClass {

    // ─── Data-driven — multiple users from Excel ─────────────────────

    /**
     * Runs the same login test multiple times with different credentials
     * from the Excel sheet.
     *
     * HOW @Test(dataProvider, dataProviderClass) works:
     *   TestNG calls DataProviderUtil.getLoginData() to get a 2D array
     *   of [username, password] pairs. Then it calls THIS test method
     *   ONCE FOR EACH ROW in that array, passing the row values as
     *   method parameters. If Excel has 3 rows, the test runs 3 times.
     *
     * WHY this matters:
     *   Without data-driven testing, you would write a separate @Test
     *   method for each set of credentials — lots of duplication.
     *   Data providers let one test cover many scenarios cleanly.

     * NEW — accepts the whole Map, then pulls values out by column name
     */
    @Test(priority = 3,
            dataProvider = "loginData",
            dataProviderClass = DataProviderUtil.class,
            description = "Login with multiple credential sets from Excel")
    public void verifyLoginWithDataProvider(Map<String, String> data) {

        // ── Read all columns from the Excel row ────────────────────────────
        // data.get() key must match Excel column header EXACTLY (case-sensitive)
        String username   = data.get("username");
        String password   = data.get("password");

        // "shouldPass" column tells us whether this row expects login to succeed.
        // Excel stores it as the text "true" or "false" — we convert to boolean.
        // Boolean.parseBoolean("true") → true
        // Boolean.parseBoolean("false") → false
        // Boolean.parseBoolean(null)   → false  (safe default if column is missing)
        boolean shouldPass = Boolean.parseBoolean(data.get("shouldPass"));

        // "testScenario" is just for logging — helps identify which row failed
        String scenario = data.get("testScenario");

        System.out.println("[LoginTest] Running scenario: " + scenario
                + " | user: " + username);

        // ── Null guard — catches Excel header spelling mistakes early ──────
        // If get() returns null, sendKeys() will crash with a confusing error.
        // This check gives you a clear message instead.
        if (username == null || password == null) {
            throw new RuntimeException(
                    "Excel column header mismatch. Map keys found: " + data.keySet()
            );
        }

        // ── Perform the login ──────────────────────────────────────────────
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);

        // ── Assert based on the shouldPass column ─────────────────────────
        if (shouldPass) {
            // This row has valid credentials — login SHOULD succeed
            Assert.assertTrue(
                    loginPage.isLoginSuccessful(),
                    "Scenario [" + scenario + "] — expected login to PASS but it failed."
            );
        } else {
            // This row has bad credentials — login SHOULD fail
            // We verify an error message appeared, meaning the system rejected the input
            Assert.assertFalse(
                    loginPage.isLoginSuccessful(),
                    "Scenario [" + scenario + "] — expected login to FAIL but it succeeded."
            );
        }
    }
}