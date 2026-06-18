package base;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * DriverFactory — creates, provides, and destroys the browser (WebDriver).
 *
 * WHY this class exists:
 *   Every test needs a browser. Instead of each test creating its own
 *   driver directly, we centralise that here. This means:
 *   - Only ONE place to change if you add a new browser.
 *   - Safe for parallel test runs (ThreadLocal — see below).
 *
 * KEY CONCEPT — ThreadLocal:
 *   Imagine running two tests at the same time. If you used a plain static
 *   WebDriver, both tests would share the SAME browser — chaos.
 *   ThreadLocal<WebDriver> gives each thread its OWN copy of the driver.
 *   Thread 1 gets its browser. Thread 2 gets a separate browser.
 *   They never interfere with each other.
 *
 *   Think of ThreadLocal like a locker room: every person (thread) has
 *   their own locker (driver). get() opens YOUR locker, not anyone else's.
 */
public class DriverFactory {

    // ThreadLocal stores one WebDriver per thread.
    // 'private' — nothing outside this class should touch it directly.
    // 'final'   — the ThreadLocal container itself never changes, only its contents.
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Returns the WebDriver for the current thread.
     * Call this anywhere you need the browser: DriverFactory.getDriver()
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get(); // get() retrieves THIS thread's driver
    }

    /**
     * Creates a new browser based on the "browser" value in config.properties.
     * Stores it in ThreadLocal so each thread keeps its own instance.
     *
     * Called by BaseClass @BeforeMethod — so a fresh browser opens before each test.
     */
    public static void initDriver() {
        String browser = ConfigReader.get("browser");

        // Defensive check — catch a missing or blank config value early
        if (browser == null || browser.trim().isEmpty()) {
            throw new RuntimeException(
                    "[DriverFactory] 'browser' key is missing or empty in config.properties."
            );
        }

        // switch-case chooses which browser to create.
        // .toLowerCase() means "Chrome", "CHROME", "chrome" all work.
        switch (browser.trim().toLowerCase()) {

            case "chrome":
                // ChromeDriver() automatically finds chromedriver via WebDriverManager
                // (if you have it in pom.xml) or from PATH.
                driverThreadLocal.set(new ChromeDriver());
                break;

            case "firefox":
                driverThreadLocal.set(new FirefoxDriver());
                break;

            case "edge":
                driverThreadLocal.set(new EdgeDriver());
                break;

            default:
                // If someone types "safari" or makes a typo, we stop immediately
                // with a helpful message rather than a cryptic NullPointerException.
                throw new RuntimeException(
                        "[DriverFactory] Unsupported browser: '" + browser +
                                "'. Valid options: chrome, firefox, edge"
                );
        }

        System.out.println("[DriverFactory] Browser launched: " + browser);
    }

    /**
     * Quits the browser and removes the driver from ThreadLocal.
     *
     * WHY remove from ThreadLocal?
     *   Thread pools reuse threads. If you only call quit() but not remove(),
     *   the next test that reuses this thread will get a dead driver reference.
     *   remove() cleans the slot so the next initDriver() starts fresh.
     *
     * Called by BaseClass @AfterMethod — runs after every test automatically.
     */
    public static void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();           // closes the browser window
            driverThreadLocal.remove();   // cleans up the ThreadLocal slot
            System.out.println("[DriverFactory] Browser closed.");
        }
    }
}
