package Base;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

    private static ThreadLocal <WebDriver> driver = new ThreadLocal<>();
    //private static WebDriver driver;

    public static WebDriver getDriver() {
        //return driver;
        return driver.get();
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser");

        if (browser.equalsIgnoreCase("Chrome")) {
            //driver = new ChromeDriver();
            driver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("Firefox")) {
            driver.set(new FirefoxDriver());
        }else if (browser.equalsIgnoreCase("Edge")) {
            driver.set(new EdgeDriver());
        }else {
            throw new RuntimeException("Invalid Browser");
        }
        //driver.manage().window().maximize();
        getDriver().manage().window().maximize();
    }
    public static void tearDown(){
       // if (driver != null){
        //    driver.quit();
        if (getDriver() != null){
            getDriver().quit(); // closes the browser
           driver.remove(); // closes the thread local and protect memory leaks
        }
    }
}
