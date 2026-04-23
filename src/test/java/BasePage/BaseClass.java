package BasePage;

import DriverPage.DriverFactory;
import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseClass {
    WebDriver driver;

    @BeforeSuite
    public void loadConfig(){
        ConfigReader.loadProperties();
    }

    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();
        DriverFactory.getDriver().get(ConfigReader.get("Url"));
    }
    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

}