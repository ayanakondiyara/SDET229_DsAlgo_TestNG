package base;

import utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.time.Duration;

public class BaseClass {
    protected WebDriver driver;

    //public BaseClass(WebDriver Driver){
    //  this.driver = driver;
    // }

    @BeforeSuite
    public void loadConfig() {
        ConfigReader.loadProperties();
        System.out.println("Config loaded");
    }

    @BeforeMethod // runs before EACH test method
    //@Parameters("browser")
    public void setUp() {

        DriverFactory.initDriver();              // calls initDriver() from Driver factory to start/initialize webdriver for tests
        driver = DriverFactory.getDriver();     // returns the started browser
        driver.get(ConfigReader.get("url"));     // opens the url
        driver.manage().window().maximize();      // maximizes the window
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Applies global wait upto 5 secs before throwing NoSuchElementException

    }

    @AfterClass
    public void tearDown() {        // quits the browser and remove thread local
        DriverFactory.tearDown();
        System.out.println("Browser closed");
    }

}