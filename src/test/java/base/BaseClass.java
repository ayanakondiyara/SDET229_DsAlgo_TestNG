package base;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class BaseClass {
    protected WebDriver driver;

    //public BaseClass(WebDriver Driver){
    //  this.driver = driver;
    // }

    @BeforeSuite
    public void loadConfig(){
        ConfigReader.loadProperties();
    }
    @BeforeMethod
    public void setUp() {
        // DriverFactory.initDriver();
        // DriverFactory.getDriver().get(ConfigReader.get("Url"));

        base.DriverFactory.initDriver();              // creates browser based on config reader
        driver = base.DriverFactory.getDriver();     // returns the browser for this thread
        driver.get(ConfigReader.get("url"));     // opens the url
        driver.manage().window().maximize();      //  maximise window
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5)); // global wait
    }
    @AfterMethod
    public void tearDown(){        // quits the browser and remove thread local
        base.DriverFactory.tearDown();
    }

}