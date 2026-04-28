package Base;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

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

        DriverFactory.initDriver();              // creates browser based on config reader
        driver = DriverFactory.getDriver();     // returns the browser for this thread
        driver.get(ConfigReader.get("Url"));     // opens the url
    }
    @AfterMethod
    public void tearDown(){        // quits the browser and remove thread local
       DriverFactory.tearDown();
    }

}