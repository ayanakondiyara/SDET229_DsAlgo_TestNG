package Utilities;

import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Map;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * DataProviderUtil — supplies test data to @Test methods from Excel.
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WHY Map<String, String> INSTEAD OF SEPARATE STRING PARAMETERS?
 *
 *   OLD WAY — fixed columns, breaks when you add a column:
 *     Object[][] data = { {"user1", "pass1"}, {"user2", "pass2"} }
 *     → test method: public void myTest(String username, String password)
 *     → adding "Role" column means changing BOTH this class AND every test method
 *
 *   NEW WAY — Map, add columns freely without changing test signatures:
 *     Object[][] data = { { Map{"Username":"u1","Password":"p1","Role":"admin"} } }
 *     → test method: public void myTest(Map<String, String> data)
 *     → adding "Role" column: just add it to Excel, then call data.get("Role")
 *       No signature changes. No resizing arrays here.
 *
 * HOW Map-BASED PROVIDERS WORK:
 *   ExcelReader.getData("SheetName") returns: List<Map<String, String>>
 *   Each Map = one Excel row. Key = column header. Value = cell content.
 *
 *   We wrap each Map in a 1-element Object[]:
 *     data[i][0] = rows.get(i)   ← the whole Map is the single value
 *
 *   TestNG passes that Map to the test as: Map<String, String> data
 *   The test reads values with: data.get("ColumnHeaderName")
 *
 * ─────────────────────────────────────────────────────────────────────────
 * EXCEL SHEET STRUCTURES
 * ─────────────────────────────────────────────────────────────────────────
 *
 *   Sheet "Login":
 *   | Username         | Password   | ShouldLoginPass | ExpectedRole |
 *   | sgs123@gmail.com | Stars@123  | true            | user         |
 *   | baduser@test.com | wrongPass  | false           | -            |
 *
 *   Sheet "PythonCode":
 *   | Code                     | ExpectedOutput | IsInvalidCode | TestScenario |
 *   | print('Hello Array')     | Hello Array    | false         | basic print  |
 *   | print(2 + 3)             | 5              | false         | addition     |
 *   | print 'hello'            |                | true          | syntax error |
 *
 *   Sheet "PracticeQuestions":
 *   | QuestionNumber | Code                        | ExpectedOutput | ShouldPassSubmit |
 *   | 1              | def search(input_list,num):\n    if num... | Element Found | true |
 *   | 2              | def findMaxConsecutiveOnes...\n...         | 3             | true |
 *
 *   → Add more columns to ANY sheet at any time.
 *     Only the test that reads them needs updating — not this file.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class DataProviderUtil {

    // ═══════════════════════════════════════════════════════════════════════
    // DATA PROVIDER 1 — Login credentials
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Reads the "Login" sheet and returns one Map per row.
     *
     * HOW TO USE IN A TEST:
     *   @Test(dataProvider = "loginData", dataProviderClass = DataProviderUtil.class)
     *   public void verifyLogin(Map<String, String> data) {
     *       String username = data.get("Username");        // Excel column header
     *       String password = data.get("Password");
     *
     *       // Extra columns — just add them to Excel and call data.get():
     *       boolean shouldPass = Boolean.parseBoolean(data.get("ShouldLoginPass"));
     *       String role        = data.get("ExpectedRole");
     *
     *       loginPage.login(username, password);
     *
     *       if (shouldPass) {
     *           Assert.assertTrue(loginPage.isLoginSuccessful());
     *       } else {
     *           Assert.assertFalse(loginPage.isLoginSuccessful());
     *       }
     *   }
     *
     * @return  Object[][] — each row is { Map<String,String> }
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        List<Map<String, String>> rows = ExcelReader.getData("Login");

        // [rows.size()] = one slot per Excel data row
        // [1]           = one value per slot (the whole Map)
        Object[][] data = new Object[rows.size()][1];

        for (int i = 0; i < rows.size(); i++) {
            // Putting the ENTIRE row Map into slot [0].
            // Example Map: {"Username":"sgs123@gmail.com", "Password":"Stars@123", ...}
            data[i][0] = rows.get(i);
        }

        return data;
        // TestNG calls your test once per row:
        //   run 1: myTest({"Username":"sgs123@gmail.com","Password":"Stars@123",...})
        //   run 2: myTest({"Username":"baduser@test.com","Password":"wrongPass",...})
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DATA PROVIDER 2 — Python code for the Try Editor
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Reads the "PythonCode" sheet and returns one Map per row.
     *
     * HOW TO USE IN A TEST:
     *   @Test(dataProvider = "pythonCodeData", dataProviderClass = DataProviderUtil.class)
     *   public void verifyTryEditor(Map<String, String> data) {
     *       // Replace literal \n from Excel with a real newline character.
     *       // Excel stores: "arr=[1,2,3]\nprint(arr[0])"
     *       // After replace: becomes two actual lines of Python code.
     *       String code     = data.get("Code").replace("\\n", "\n");
     *       String expected = data.get("ExpectedOutput");
     *       boolean invalid = Boolean.parseBoolean(data.get("IsInvalidCode"));
     *
     *       if (invalid) {
     *           String alert = editor.runInvalidCodeAndGetAlertMessage(code);
     *           Assert.assertFalse(alert.isEmpty(), "Expected error alert");
     *       } else {
     *           editor.runCode(code);
     *           Assert.assertEquals(editor.getOutput(), expected);
     *       }
     *   }
     *
     * NOTE ON \n IN EXCEL:
     *   You cannot press Enter inside an Excel cell to add a real newline
     *   (or it's awkward). Instead type \n as two characters in the cell.
     *   The replace("\\n", "\n") in your test converts it to a real newline.
     *
     * @return  Object[][] — each row is { Map<String,String> }
     */
    @DataProvider(name = "pythonCodeData")
    public Object[][] getPythonCodeData() {
        List<Map<String, String>> rows = ExcelReader.getData("PythonCode");

        Object[][] data = new Object[rows.size()][1];

        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
            // Example Map: {"Code":"print('Hello')", "ExpectedOutput":"Hello",
            //               "IsInvalidCode":"false", "TestScenario":"basic print"}
        }

        return data;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DATA PROVIDER 3 — Practice Questions
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Reads the "PracticeQuestions" sheet and returns one Map per row.
     *
     * HOW TO USE IN A TEST:
     *   @Test(dataProvider = "practiceQuestionData", dataProviderClass = DataProviderUtil.class)
     *   public void verifyPracticeQuestion(Map<String, String> data) {
     *       int    questionNum  = Integer.parseInt(data.get("QuestionNumber"));
     *       String code         = data.get("Code").replace("\\n", "\n");
     *       String expected     = data.get("ExpectedOutput");
     *       boolean shouldPass  = Boolean.parseBoolean(data.get("ShouldPassSubmit"));
     *
     *       QuestionPage page = navigateToQuestion(questionNum);
     *       page.runCode(code);
     *       Assert.assertFalse(page.getResultPanelText().isEmpty());
     *
     *       page.submitCode(code);
     *       if (shouldPass) {
     *           Assert.assertTrue(page.isSubmissionSuccessful());
     *       } else {
     *           Assert.assertFalse(page.isSubmissionSuccessful());
     *       }
     *   }
     *
     * @return  Object[][] — each row is { Map<String,String> }
     */
    @DataProvider(name = "PracticeQData")
    public Object[][] getPracticeQData() {
        List<Map<String, String>> rows = ExcelReader.getData("PracticeQuestion");

        Object[][] data = new Object[rows.size()][1];

        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }

        return data;
    }


    @DataProvider(name = "ArrayTryData")
    public Object[][] getArrayTryData() {

        List<Map<String, String>> rows = ExcelReader.getData("Array_Try");

        Object[][] data = new Object[rows.size()][3];

        for (int i = 0; i < rows.size(); i++) {

            Map<String, String> row = rows.get(i);

            data[i][0] = row.get("SubModule");
            data[i][1] = row.get("Code");
            data[i][2] = row.get("ExpectedResult");
        }

        return data;
    }

    @DataProvider(name = "LinkedListTryData")
    public Object[][] getLinkedListTryData() {
        List<Map<String, String>> rows = ExcelReader.getData("LinkedList_Try");

        Object[][] data = new Object[rows.size()][3];

        for (int i = 0; i < rows.size(); i++) {

            Map<String, String> row = rows.get(i);

            data[i][0] = row.get("SubModule");
            data[i][1] = row.get("Code");
            data[i][2] = row.get("ExpectedResult");
        }

        return data;
    }

    @DataProvider(name = "LinkedListPracticeData")
    public Object[][] getLinkedListPracticeQData() {
        List<Map<String, String>> rows = ExcelReader.getData("LinkedList_Practice");

        Object[][] data = new Object[rows.size()][2];

        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = rows.get(i);

            data[i][0] = row.get("Submodule");
            data[i][1] = row.get("PracticeUrl");
        }

        return data;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // HOW TO ADD A NEW DATA PROVIDER FOR A NEW MODULE (e.g. Stack)
    // ═══════════════════════════════════════════════════════════════════════
    //
    // Step 1 — Add a sheet "Stack" to TestData.xlsx with your columns.
    //
    // Step 2 — Copy any method above, change two things:
    //
    //   @DataProvider(name = "stackData")             ← unique name
    //   public Object[][] getStackData() {
    //       List<Map<String, String>> rows = ExcelReader.getData("Stack"); ← sheet
    //       Object[][] data = new Object[rows.size()][1];
    //       for (int i = 0; i < rows.size(); i++) {
    //           data[i][0] = rows.get(i);
    //       }
    //       return data;
    //   }
    //
    // Step 3 — In your test:
    //   @Test(dataProvider = "stackData", dataProviderClass = DataProviderUtil.class)
    //   public void verifyStack(Map<String, String> data) {
    //       String code = data.get("Code").replace("\\n", "\n");
    //       ...
    //   }
    //
    // The body of every data provider is identical — only the name and
    // sheet name change. Three lines of real logic, rest is just naming.
    // ═══════════════════════════════════════════════════════════════════════
}