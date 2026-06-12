package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashBoardPage {

    WebDriver driver;

    public void DashBoardPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickGetStarted() {
        driver.findElement(By.xpath("//button[contains(text(),'Get Started')]")).click();
    }
}
