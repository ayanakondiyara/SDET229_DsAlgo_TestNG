package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseClass {
    // ThreadLocal is used to make sure that each thread has its own driver instance
    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    @Parameters("browser")
    @BeforeMethod
    public void setup(String browserName) {
        if (browserName.equalsIgnoreCase("chrome")) {
            tlDriver.set(new ChromeDriver());
        } else if (browserName.equalsIgnoreCase("firefox")) {
            tlDriver.set(new FirefoxDriver());
        } else if (browserName.equalsIgnoreCase("edge")) {
            tlDriver.set(new EdgeDriver());
        }

        getDriver().get("https://dsportalapp.herokuapp.com/");
        getDriver().manage().window().maximize();
    }

    // Helper method to get the driver
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    @AfterMethod
    public void tearDown() {
        getDriver().quit();
        tlDriver.remove(); // to clean up the thread
    }
}