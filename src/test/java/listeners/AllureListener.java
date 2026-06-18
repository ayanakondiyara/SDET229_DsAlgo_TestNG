package listeners;

import base.DriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * AllureListener — a TestNG listener that captures a screenshot every time
 * a test fails and attaches it to the Allure report.
 *
 * WHY a listener?
 *   A "listener" is a class that TestNG notifies about test events:
 *   test started, test passed, test failed, etc. You implement the
 *   events you care about and ignore the rest.
 *
 *   Without a listener, you would have to add screenshot code to the
 *   catch block of every single test — lots of duplication. The listener
 *   intercepts failures automatically for ALL tests from one place.
 *
 * HOW to register this listener:
 *   In testng.xml (already done in yours):
 *     <listeners>
 *       <listener class-name="listeners.AllureListener"/>
 *     </listeners>
 *
 *   OR with an annotation on a test class:
 *     @Listeners(AllureListener.class)
 *
 * WHAT is Allure?
 *   Allure is a test reporting library that produces a rich HTML report
 *   with graphs, steps, timelines, and attachments like screenshots.
 *   Run 'allure serve target/allure-results' after your tests to view it.
 *
 * INTERFACE ITestListener:
 *   Provides callback methods for: onTestStart, onTestSuccess, onTestFailure,
 *   onTestSkipped, onTestFailedWithTimeout, and suite-level events.
 *   We only override the ones we need — Java 8+ allows this via default methods.
 */
public class AllureListener implements ITestListener {

    /**
     * Called by TestNG automatically whenever a @Test method fails.
     *
     * ITestResult contains everything about the failed test:
     *   result.getName()          → test method name
     *   result.getThrowable()     → the exception that caused the failure
     *   result.getParameters()    → data-provider values used for this run
     *
     * We call saveScreenshot() here to capture the browser state at
     * the exact moment of failure — invaluable for diagnosing what went wrong.
     *
     * @param result  information about the failed test
     */
    @Override
    public void onTestFailure(ITestResult result) {
        // Log the failure with our custom logger
        Utilities.LoggerLoad.error("TEST FAILED: " + result.getName());

        if (result.getThrowable() != null) {
            Utilities.LoggerLoad.error("Cause: " + result.getThrowable().getMessage());
        }

        // Capture and attach the screenshot
        byte[] screenshot = captureScreenshot();
        if (screenshot != null) {
            // Allure.addAttachment() attaches the screenshot to the current
            // test's entry in the Allure report under the name "Screenshot on Failure"
            Allure.addAttachment(
                    "Screenshot on Failure — " + result.getName(),
                    new ByteArrayInputStream(screenshot)
            );
        }
    }

    /**
     * Called by TestNG whenever a @Test method is skipped.
     *
     * Tests are skipped when a dependency test fails (dependsOnMethods)
     * or when they are excluded by groups. Logging helps track these.
     *
     * @param result  information about the skipped test
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        Utilities.LoggerLoad.warn("TEST SKIPPED: " + result.getName());
    }

    /**
     * Called by TestNG whenever a @Test method passes.
     * Optional — only log if you want a success trail in your console.
     *
     * @param result  information about the passed test
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        Utilities.LoggerLoad.info("TEST PASSED: " + result.getName());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SCREENSHOT HELPER
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Takes a screenshot of the current browser state and returns it as bytes.
     *
     * WHY byte array?
     *   Allure's attachment API accepts a byte[]. OutputType.BYTES tells
     *   Selenium to return the screenshot directly as bytes instead of
     *   saving it to a temporary file first (OutputType.FILE).
     *
     * WHY @Attachment?
     *   The @Attachment annotation is an alternative Allure method of
     *   attaching screenshots — the return value of the annotated method
     *   is automatically attached to the report. We also call
     *   Allure.addAttachment() manually in onTestFailure() for more control
     *   over the attachment name. Either approach works; here we keep both
     *   to demonstrate both patterns.
     *
     * @return  PNG screenshot as a byte array, or null if capture fails
     */
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] captureScreenshot() {
        try {
            // TakesScreenshot is an interface that WebDriver implements.
            // We cast driver to it to access getScreenshotAs().
            // OutputType.BYTES returns the image as raw bytes (PNG format).
            return ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);

        } catch (Exception e) {
            // This can fail if the driver is null (e.g. browser crashed).
            // Log the problem but don't throw — we don't want a screenshot
            // failure to obscure the original test failure.
            Utilities.LoggerLoad.error("Could not capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
