package com.L2.static_tools;

import org.apache.poi.ss.usermodel.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ExcelTools {
//    private static void processSheet(Workbook workbook, Connection conn, String sheetName, int numColumns, List<Integer> columnIndices) throws SQLException {
//        Sheet sheet = workbook.getSheet(sheetName);
//        if (sheet == null) {
//            System.out.println("Sheet " + sheetName + " not found.");
//            return;
//        }
//
//        // Create table dynamically based on sheet name and number of columns
//        String tableName = sheetName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
//        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
//        for (int i = 0; i < numColumns; i++) {
//            createTableSQL.append("col").append(i).append(" TEXT");
//            if (i < numColumns - 1) createTableSQL.append(", ");
//        }
//        createTableSQL.append(")");
//        try (PreparedStatement stmt = conn.prepareStatement(createTableSQL.toString())) {
//            stmt.execute();
//        }
//
//        // Prepare insert statement
//        StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
//        for (int i = 0; i < numColumns; i++) {
//            insertSQL.append("?");
//            if (i < numColumns - 1) insertSQL.append(", ");
//        }
//        insertSQL.append(")");
//        PreparedStatement insertStmt = conn.prepareStatement(insertSQL.toString());
//
//        // Iterate through rows
//        int rowCount = 0;
//        for (Row row : sheet) {
//            // Skip header row if needed (uncomment if your data has headers)
//            // if (rowCount == 0) { rowCount++; continue; }
//
//            // Clear previous parameters
//            insertStmt.clearParameters();
//
//            // Read only specified columns
//            for (int i = 0; i < numColumns; i++) {
//                int colIndex = columnIndices.get(i);
//                Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                String cellValue = getCellValueAsString(cell);
//                insertStmt.setString(i + 1, cellValue);
//            }
//
//            // Execute insert
//            insertStmt.executeUpdate();
//
//            rowCount++;
//            if (rowCount % 1000 == 0) {
//                System.out.println("Processed " + rowCount + " rows for " + sheetName);
//            }
//        }
//
//        System.out.println("Finished processing " + rowCount + " rows for " + sheetName);
//        insertStmt.close();
//    }

    public static boolean getSheet3(Workbook workbook) {
        // Get the "Product to Spares" sheet (third sheet, index 2)
        Sheet sheet = workbook.getSheet("Product to Spares");
        if (sheet == null) {
            System.out.println("Sheet 'Product to Spares' not found.");
            return false;
        }
        // Counter for rows processed
        int rowCount = 0;
        // Iterate through the first 10 rows
        for (Row row : sheet) {
            if (rowCount >= 10) {
                break; // Stop after 10 rows
            }
            // Build a string for the row
            StringBuilder rowData = new StringBuilder("Row " + rowCount + ": ");
            int colCount = 0;
            for (Cell cell : row) {
                String cellValue = getCellValueAsString(cell);
                rowData.append(colCount + ")" + cellValue).append("\t");
                colCount++;
            }
            // Print the row
            System.out.println(rowData.toString());
            rowCount++;
        }

        System.out.println("Printed " + rowCount + " rows from 'Product to Spares'.");
        return true;
    }

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Format numbers to avoid scientific notation
                return String.format("%.8f", cell.getNumericCellValue()).replaceAll("\\.0+$", "");
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }
}
