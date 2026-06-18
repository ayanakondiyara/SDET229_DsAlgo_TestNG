package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * RetryAnalyzer — retries a failed @Test up to MAX_RETRY_COUNT times.
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WHY RETRY?
 *   Some failures are not real bugs. They are "flaky" — caused by:
 *     - Slow network (element took 6s but wait was set to 5s)
 *     - Heroku app sleeping on first hit (cold start delay)
 *     - JavaScript-heavy pages not fully rendering in time
 *
 *   Retrying filters out these one-off failures so the final report
 *   only shows consistently failing tests — real bugs worth investigating.
 *
 * HOW IT CONNECTS TO YOUR TESTS:
 *   RetryAnnotationTransformer (its own file) applies this class to every
 *   @Test automatically via testng.xml. You do not need to add
 *   retryAnalyzer = RetryAnalyzer.class to each @Test manually.
 *
 * HOW IRetryAnalyzer WORKS:
 *   After each test failure, TestNG calls retry(result).
 *   If retry() returns true  → run the test again.
 *   If retry() returns false → mark the test as FAILED in the report.
 *
 * NOTE: 'count' is an INSTANCE variable (not static).
 *   TestNG creates a NEW RetryAnalyzer instance for each test method,
 *   so 'count' always starts at 0 for each test. Tests do not share counters.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    // Tracks how many retries have been used for the CURRENT test.
    // Starts at 0, increments by 1 each time retry() returns true.
    private int count = 0;

    // Maximum number of RETRY attempts (not counting the original run).
    // count < 2 means: allow retry on attempt 1 and attempt 2.
    // Total runs possible per test: 1 original + 2 retries = 3 maximum.
    private static final int MAX_RETRY_COUNT = 2;

    /**
     * Called by TestNG after each failure.
     *
     * @param result  details about the failed test (name, exception, etc.)
     * @return        true = retry this test | false = give up, mark as FAILED
     */
    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRY_COUNT) {
            count++;
            Utilities.LoggerLoad.warn(
                    "Retrying: '" + result.getName() +
                            "' — attempt " + count + " of " + MAX_RETRY_COUNT
            );
            return true;
        }
        return false;
    }
}