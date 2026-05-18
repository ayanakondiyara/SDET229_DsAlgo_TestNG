package Base;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
}
    public WebDriverWait getWait(long timeOut) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut));
    }

    public void waitForElementToClick(WebElement element, long timeOutInSec) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);",element);
        getWait(timeOutInSec).until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void waitForElementToDisplayed(WebElement element, long timeOutInSec) {
        getWait(timeOutInSec).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForUrl(String expectedUrl, long timeOutInSec) {
        getWait(timeOutInSec).until(ExpectedConditions.urlToBe(expectedUrl));
    }

    public String getTooltipMsg(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String toolTipMsg = (String) js.executeScript("return arguments[0].validationMessage;", element);
        return toolTipMsg;
    }

    public void enterCodeInEditor(String code) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);",
                code
        );
    }

    public void waitForTitle(String title, long timeOutInSec) {
        getWait(timeOutInSec).until(ExpectedConditions.or(ExpectedConditions.titleIs(title),
                ExpectedConditions.titleContains(title)));
    }

    {
    //public List<Map<String, String>> readDataFromExcel(String sheetName, Integer rowNumber) throws IOException, InvalidFormatException {
       // ExcelReader reader = new ExcelReader();
       // return reader.getData(filePath, sheetName);
    }

    public boolean waitForPageToLoad() {
        return getWait(15).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                        .equals("complete")
        );
    }
    public @Nullable Alert getAlert(long timeOutInSec) {
        getWait(timeOutInSec).until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert();
    }

}

