package testCases;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.HomePage;
import pageObjects.RegistrationPage;
import utilities.TestDataProvider;
import java.util.Map;

public class RegistrationTest extends BaseClass {

    private RegistrationPage reg;
    private HomePage home;

    @BeforeMethod
    public void setUpPages() {
        super.setUp();

        home = new HomePage();
        reg = new RegistrationPage();

        home.clickMainGetStarted();
        home.clickRegisterLink();
    }

    private void submit(Map<String, String> data) {
        reg.register(
                data.get("Username"),
                data.get("Password"),
                data.get("Confirm Password")
        );
    }

    private String alert() {
        return reg.waitForAlertMessage();
    }

    //Test 1
    @Test(dataProvider = "RegistrationData", dataProviderClass = TestDataProvider.class)
    public void verifySuccessfulRegistration(Map<String, String> data) {

        if (!data.get("Scenario").equals("verifySuccessfulRegistration")) return;

        submit(data);

        String actual = alert();
        String expected = "New Account Created. You are logged in as " + data.get("Username");

        Assert.assertEquals(actual, expected);
        Assert.assertEquals(driver.getTitle(), "NumpyNinja");
    }

    //Test 2
        @Test(dataProvider = "RegistrationData",dataProviderClass = TestDataProvider.class)
        public void verifyTooltipMsgWithOnlyUsername(Map<String, String> data) {

            if (!data.get("Scenario").equals("VerifyTooltipMsgWithOnlyUsername")) return;

            submit(data);
            Assert.assertEquals(reg.validatePasswordTooltipMsg(), "Please fill out this field.");
            String actualTooltip = reg.validatePasswordTooltipMsg();

        }

        //Test 3
        @Test(dataProvider = "RegistrationData",dataProviderClass = TestDataProvider.class)
        public void verifyTooltipMsgWithOnlyPassword(Map<String, String> data) {

            if (!data.get("Scenario").equals("VerifyTooltipMsgWithOnlyPassword")) return;

            submit(data);
            Assert.assertEquals(reg.validateUsernameTooltipMsg(), "Please fill out this field.");
        }

        //Test 4
        @Test(dataProvider = "RegistrationData",dataProviderClass = TestDataProvider.class)
        public void verifyTooltipMsgWithOnlyUsernameAndPassword(Map<String, String> data) {

            if (!data.get("Scenario").equals("VerifyTooltipMsgWithOnlyUsernameAndPassword")) return;

            submit(data);
            Assert.assertEquals(reg.validateConfirmPwdTooltipMsg(), "Please fill out this field.");
        }

    //Test 5
    @Test(dataProvider = "RegistrationData",dataProviderClass = TestDataProvider.class)
    public void verifyTooltipMsgWithUnmatchedPassword(Map<String, String> data) {

        if (!data.get("Scenario").equals("VerifyTooltipMsgWithUnmatchedPassword")) return;

        submit(data);

        String actual = alert();
        String expected = "password_mismatch:The two password fields didn’t match.";

        Assert.assertEquals(actual, expected);
    }
    }
