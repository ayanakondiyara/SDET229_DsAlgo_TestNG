package utilities;

import org.testng.annotations.DataProvider;
import java.util.List;
import java.util.Map;

public class TestDataProvider {

    @DataProvider(name = "RegistrationData")
    public Object[][] registrationData() {

        String filePath = System.getProperty("user.dir") +
                "/src/test/resources/TestData/TESTNG_TestData.xlsx";

       ExcelReader excel = new ExcelReader(filePath, "Registration");
        List<Map<String, String>> dataList = excel.getDataList();

        Object[][] data = new Object[dataList.size()][1];

        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }

        return data;
    }
}