package utilities;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    private static Workbook workbook;
    private static Sheet sheet;

    public ExcelReader(String filePath, String sheetName) { // method to load excel using file path
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/TestData/TESTNG_TestData.xlsx"); // FileInputStream helps java to open excel file
            workbook = WorkbookFactory.create(fis); // creates a workbook obj for entire Excel file
            sheet = workbook.getSheet(sheetName);// gets the specific Sheet from the Excel file

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Excel file: " + filePath);
        }
    }

    public List<Map<String, String>> getDataList() {
        List<Map<String, String>> dataList = new ArrayList<>();// creates empty array list

        Row headerRow = sheet.getRow(0);// row 0 becomes header row that is key
        int totalRows = sheet.getPhysicalNumberOfRows();//getPhysicalNumberOfRows()-finds how many rows in a sheet
        int totalCols = headerRow.getPhysicalNumberOfCells(); // getPhysicalNumberOfCells()- finds how many columns in a row

        for (int i = 1; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            Map<String, String> rowMap = new LinkedHashMap<>();// linkedHashMap will store entire row values as key -value pair

            for (int j = 0; j < totalCols; j++) {
                String key = headerRow.getCell(j).toString();
                String value = (row == null || row.getCell(j) == null) ? "" : row.getCell(j).toString();
                // ternary operator - condition ? valueiftrue : valueif false
                rowMap.put(key, value);
            }
            dataList.add(rowMap);
        }
        return dataList;
    }
}






