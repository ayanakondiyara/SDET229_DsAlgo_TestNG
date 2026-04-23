package DriverPage;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser");

        if (browser.equalsIgnoreCase("Chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("Firefox")) {
            driver = new FirefoxDriver();
        }

        driver.manage().window().maximize();
    }
    public static void tearDown(){
        if (driver != null){
            driver.quit();
        }
    }
}
