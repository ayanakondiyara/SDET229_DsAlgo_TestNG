package Utilities;

/**
 * LoggerLoad — a simple wrapper for logging messages during test execution.
 *
 * WHY logging?
 *   System.out.println() works, but log messages from different tests run
 *   in parallel all mix together with no timestamps, no severity levels,
 *   and no easy way to filter. A logger adds:
 *     - Timestamps        (when did this happen?)
 *     - Severity levels   (INFO, WARN, ERROR — filter noise vs. real problems)
 *     - Class names       (which class printed this?)
 *
 * WHY a wrapper class?
 *   If you ever switch from print statements to a proper logging library
 *   (Log4j, SLF4J), you only change THIS class. Every other class that
 *   calls LoggerLoad.info() doesn't need to change at all.
 *
 * CURRENT IMPLEMENTATION:
 *   This uses System.out/err for simplicity — no extra dependencies needed.
 *   When you are ready to use Log4j2, replace the method bodies and add
 *   the dependency to pom.xml. Nothing else in your project changes.
 *
 * USAGE IN OTHER CLASSES:
 *   LoggerLoad.info("Browser launched successfully");
 *   LoggerLoad.warn("Element not found, retrying...");
 *   LoggerLoad.error("Login failed for user: " + username);
 */
public class LoggerLoad {

    // Private constructor — prevents anyone from doing 'new LoggerLoad()'.
    // All methods are static, so you call them on the class itself:
    //   LoggerLoad.info("message")  ✓
    //   new LoggerLoad().info(...)  ✗  (prevented by private constructor)
    private LoggerLoad() {}

    /**
     * Logs an informational message.
     * Use for normal flow events: "Browser opened", "Navigated to URL", etc.
     *
     * @param message  the log message to print
     */
    public static void info(String message) {
        System.out.println(formatLog("INFO", message));
    }

    /**
     * Logs a warning — something unexpected but not fatal.
     * Use when the test can continue but something was off:
     * "Element took longer than expected", "Retrying step..."
     *
     * @param message  the warning message
     */
    public static void warn(String message) {
        System.out.println(formatLog("WARN", message));
    }

    /**
     * Logs an error — something went wrong.
     * Use when catching exceptions or when assertions fail to add context.
     *
     * Printed to System.err so IDEs can highlight it differently.
     *
     * @param message  the error message
     */
    public static void error(String message) {
        System.err.println(formatLog("ERROR", message));
    }

    /**
     * Logs an error along with the full exception stack trace.
     * Use in catch blocks to record the cause of the failure.
     *
     * @param message    a human-readable description of what failed
     * @param throwable  the caught exception
     */
    public static void error(String message, Throwable throwable) {
        System.err.println(formatLog("ERROR", message));
        throwable.printStackTrace(System.err);
    }

    /**
     * Formats the log line with a timestamp, level, and thread name.
     *
     * Output example:
     *   [2024-05-09 14:32:01] [INFO ] [main] Browser launched: chrome
     *
     * WHY include the thread name?
     *   When tests run in parallel, each test runs on a different thread.
     *   The thread name (e.g. "TestNG-worker-0") tells you which test's
     *   log line this is when they're interleaved in the console output.
     *
     * @param level    "INFO", "WARN", or "ERROR"
     * @param message  the message body
     * @return         the formatted string ready to print
     */
    private static String formatLog(String level, String message) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String threadName = Thread.currentThread().getName();

        // %-5s left-pads the level to 5 characters so columns align:
        //   [INFO ] vs [WARN ] vs [ERROR]
        return String.format("[%s] [%-5s] [%s] %s",
                timestamp, level, threadName, message);
    }
}