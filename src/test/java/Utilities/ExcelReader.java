package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * ExcelReader — reads test data from an Excel (.xlsx) file.
 *
 * WHY read from Excel?
 *   Storing test data in Excel means non-developers (BAs, QAs, PMs)
 *   can add or edit test scenarios without touching Java code.
 *   It also enables data-driven testing: one test method runs many
 *   times with different inputs from different rows.
 *
 * LIBRARIES used (Apache POI):
 *   Apache POI is the standard Java library for reading/writing
 *   Microsoft Office files. For .xlsx files we use:
 *     XSSFWorkbook  → represents the whole Excel file
 *     Sheet         → one tab in the workbook
 *     Row           → one row on a sheet
 *     Cell          → one cell in a row

 * EXCEL STRUCTURE expected:
 *   Row 0 (header): | Username | Password |  ← column names
 *   Row 1:          | user1    | pass1    |  ← data row 1
 *   Row 2:          | user2    | pass2    |  ← data row 2

 * TWO MODES:
 *   getData()   — returns a List<Map<String,String>>. Keyed by column name.
 *                 Best for DataProviders that use Map-style access.
 *   getCellData() / getRowCount() — row+column index access.
 *                 Best for DataProviders that fill a String[][] array.

 * We provide BOTH so DataProviderUtil can use whichever fits better.
 */
public class ExcelReader {

    // File path — matches what DataProviderUtil references.
    // Using a constant means you only update the path in one place.
    private static final String FILE_PATH =
            "src/test/resources/testData.xlsx";

    // Cache: once a sheet is read, we store it here.
    // Key = sheet name, Value = the parsed data.
    // WHY cache? Reading a file from disk is slow. If two test methods
    // need the same sheet, we only read the file once.
    private static final Map<String, List<Map<String, String>>> cache = new HashMap<>();

    // ─────────────────────────────────────────────────────────────────────────
    // MODE 1 — Map-based access (recommended for readability)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Reads all data rows from the named sheet and returns them as a list of maps.
     *
     * Each map represents one data row.
     * Keys   = column header names  (from row 0)
     * Values = cell values          (as Strings)
     *
     * Example result for a "Login" sheet:
     *   [ {"Username": "user1", "Password": "pass1"},
     *     {"Username": "user2", "Password": "pass2"} ]
     *
     * Usage in DataProvider:
     *   List<Map<String,String>> rows = ExcelReader.getData("Login");
     *   String user = rows.get(0).get("Username");
     *
     * @param sheetName  the exact name of the Excel tab (case-sensitive)
     * @return           list of row maps; empty list if sheet not found or error
     */
    public static List<Map<String, String>> getData(String sheetName) {

        // Return cached version if we've read this sheet before
        if (cache.containsKey(sheetName)) {
            return cache.get(sheetName);
        }

        List<Map<String, String>> sheetData = new ArrayList<>();

        // try-with-resources: automatically closes the file when the block ends,
        // even if an exception is thrown — prevents resource leaks.
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                System.err.println("[ExcelReader] Sheet not found: " + sheetName);
                return sheetData; // return empty list, not null — safer for callers
            }

            Row headerRow = sheet.getRow(0); // row index 0 = first row = headers
            int columnCount = headerRow.getLastCellNum();

            // i starts at 1 to skip the header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // skip completely empty rows

                Map<String, String> rowMap = new LinkedHashMap<>(); // preserves column order

                for (int j = 0; j < columnCount; j++) {
                    // Get header name for this column
                    String key = getCellValueAsString(headerRow.getCell(j));
                    // Get data value for this row+column
                    String value = getCellValueAsString(row.getCell(j));
                    rowMap.put(key, value);
                }

                sheetData.add(rowMap);
            }

        } catch (IOException e) {
            System.err.println("[ExcelReader] Failed to read file: " + FILE_PATH);
            e.printStackTrace();
        }

        cache.put(sheetName, sheetData); // save to cache for future calls
        return sheetData;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MODE 2 — Row/column index access (used by DataProviderUtil array style)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns the number of data rows in a sheet (excludes the header row).
     *
     * Example: if the sheet has 1 header + 3 data rows, returns 3.
     * DataProviderUtil uses this to size the Object[][] array:
     *   Object[][] data = new Object[getRowCount("Login")][2];
     *
     * @param filePath   path to the Excel file
     * @param sheetName  the tab name
     * @return           number of data rows (0 if sheet not found or error)
     */
    public int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return 0;

            // getLastRowNum() returns the index of the last row (0-based).
            // Because row 0 is the header, the number of DATA rows = getLastRowNum().
            return sheet.getLastRowNum();

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns the String value of a single cell by row and column index.
     *
     * Example:
     *   getCellData(FILE_PATH, "Login", 1, 0) → "user1"  (row 1, col 0)
     *   getCellData(FILE_PATH, "Login", 1, 1) → "pass1"  (row 1, col 1)
     *
     * Note: rowNum is 1-based here (1 = first data row, not the header).
     *
     * @param filePath   path to the Excel file
     * @param sheetName  the tab name
     * @param rowNum     1-based data row number (1 = first data row after header)
     * @param colNum     0-based column index (0 = first column)
     * @return           cell value as String, or "" if cell is null/blank
     */
    public String getCellData(String filePath, String sheetName, int rowNum, int colNum) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return "";

            Row row = sheet.getRow(rowNum); // rowNum 1 = the first data row
            if (row == null) return "";

            Cell cell = row.getCell(colNum);
            return getCellValueAsString(cell);

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPER
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Converts any Cell to a String, handling different cell types.
     *
     * WHY this is needed:
     *   Excel cells can be NUMERIC, STRING, BOOLEAN, FORMULA, or BLANK.
     *   Calling cell.getStringCellValue() on a NUMERIC cell throws an
     *   exception. We need to check the type first and convert accordingly.
     *
     * @param cell  the Excel Cell object (may be null)
     * @return      the cell's value as a String, or "" if null/blank
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        // CellType is an enum: STRING, NUMERIC, BOOLEAN, FORMULA, BLANK, ERROR
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                // Numbers in Excel are stored as doubles (e.g. 1.0, 42.0).
                // DataFormatter converts them to the display string
                // (e.g. "1" not "1.0", date cells as "01/01/2024", etc.)
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell).trim();

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                // For formula cells, evaluate and return the cached result as string
                return cell.getCachedFormulaResultType() == CellType.NUMERIC
                        ? String.valueOf((long) cell.getNumericCellValue())
                        : cell.getStringCellValue().trim();

            case BLANK:
            default:
                return "";
        }
    }
}