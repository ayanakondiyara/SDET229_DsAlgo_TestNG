package testCases;

import Base.BaseClass;
import Base.BasePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.RegistrationPage;

public class RegistrationTest extends BaseClass {
    private RegistrationPage reg;
    @BeforeMethod
    public void initPages(){
        super.setUp();
        reg = new RegistrationPage();

    }

   @DataProvider(name = "emptyFields")
    public Object[][] emptyFields(){
        return new Object[][]{
            {"","","","Please fill out this field."}
        };
    }

    @DataProvider(name = "OnlyUsername")
    public Object[][] OnlyUsername(){
        return new Object[][]{
                {"Sgs@gmail","Please fill out this field."}
        };
    }

    @DataProvider(name = "UsernameAndPassword")
    public Object[][] UsernameAndPassword(){
        return new Object[][]{
                {"Sgs@gmail","Stars123","Please fill out this field."}
        };
    }

    @DataProvider(name = "MismatchedPassword")
    public Object[][] MismatchedPassword(){
        return new Object[][]{
                {"Sgs@gmail","Stars123","strr234","password_mismatch:The two password fields didn’t match."}
        };
    }

    @DataProvider(name = "invalidUsernames")
    public Object[][] invalidUsernames() {
        return new Object[][]{
                {"}}}}}}}}}}}}}}", "Stars@123", "Stars@123", "password_mismatch:The two password fields didn’t match."},
                {"**************", "Stars@123", "Stars@123", "password_mismatch:The two password fields didn’t match."},
                {"%%%!!!!4555", "Stars@123", "Stars@123", "password_mismatch:The two password fields didn’t match."},
                {"?????????]]]]", "**&^{{{{{Stars@123}}}}}", "**&^{{{{{Stars@123}}}}}", "password_mismatch:The two password fields didn’t match."}
        };
    }

    @DataProvider(name = "ValidFields")
    public Object[][] ValidFields(){
        return new Object[][]{
                {"Sgs@gmail", "Stars123", "strr234", "New Account Created. You are logged in as <Username> "}
        };
    }

    @Test(dataProvider = "emptyFields")
    public void verifyTooltipMsgWhenAllFieldsEmpty(String username, String password, String confirmPwd, String expectedTooltip) {
        reg.enterValues(username, password, confirmPwd);
        reg.clickRegisterBtn();
        Assert.assertEquals(reg.validateUsernameTooltipMsg(), expectedTooltip);
    }

    @Test(dataProvider = "OnlyUsername")
    public void verifyTooltipMsgWhenOnlyUsernameEntered(String username,String expectedTooltip) {
        reg.enterUsername(username);
        reg.clickRegisterBtn();
        Assert.assertEquals(reg.validateUsernameTooltipMsg(), expectedTooltip);
    }

    @Test(dataProvider = "UsernameAndPassword")
    public void verifyTooltipMsgWhenUsernameAndPasswordEntered(String username,String password,String confirmPwd,String expectedTooltip) {
        reg.enterValues(username,password,confirmPwd );
        reg.clickRegisterBtn();
        Assert.assertEquals(reg.validateUsernameTooltipMsg(), expectedTooltip);
    }

    @Test(dataProvider = "MismatchedPassword")
    public void verifyTooltipMsgWhenMismatchedPasswordEntered(String username,String password,String confirmPwd,String expectedTooltip) {
        reg.enterValues(username,password,confirmPwd);
        reg.clickRegisterBtn();
        Assert.assertEquals(reg.validateUsernameTooltipMsg(), expectedTooltip);
    }


        @Test(dataProvider ="invalidUsernames")
        public void verifyTooltipMsgWheninvalidUsernamesEntered(String username, String password, String confirmPwd, String expectedTooltip){

            reg.enterValues(username, password, confirmPwd);
            reg.clickRegisterBtn();
            Assert.assertEquals(reg.validateUsernameTooltipMsg(), expectedTooltip);
        }



    @Test(dataProvider = "ValidFields")
    public void verifyUserAbleToCreateAccount(String username,String password,String confirmPwd,String expectedTooltip) {
        reg.enterValues(username,password,confirmPwd);
        reg.clickRegisterBtn();
        Assert.assertEquals(reg.validateUsernameTooltipMsg(), expectedTooltip);
    }
}


