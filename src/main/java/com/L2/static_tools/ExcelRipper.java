package com.L2.static_tools;

import com.L2.dto.global_spares.ProductToSpares;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.repository.interfaces.GlobalSparesRepository;
import org.apache.poi.ss.usermodel.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelRipper {


    public static boolean extractProductToSparesSheet(Workbook workbook) {
        GlobalSparesRepository globalSparesRepository = new GlobalSparesRepositoryImpl();
        Sheet sheet = workbook.getSheet("Product to Spares");
        if (sheet == null) {
            System.out.println("Sheet 'Product to Spares' not found.");
            return false;
        }
        // Iterate through the first 10 rows
        for (Row row : sheet) {
            // this is temp for testing
//            if (row.getRowNum() >= 10) {
//                break; // Stop after 10 rows
//            }
            // we will not start writing until we get to row three
            if (row.getRowNum() < 3) {
                continue;
            }
            // start rowCount when we begin

            StringBuilder rowData = new StringBuilder("Row " + row.getRowNum() + ": ");
            ProductToSpares productToSpares = new ProductToSpares();
            int colCount = 0;
            for (Cell cell : row) {
                String cellValue = getCellValueAsString(cell);
                switch (colCount) {
                    case 0 -> productToSpares.setPimRange(cellValue);
                    case 1 -> productToSpares.setPimProductFamily(cellValue);
                    case 2 -> productToSpares.setSpareItem(cellValue);
                    case 3 -> productToSpares.setReplacementItem(cellValue);
                    case 4 -> productToSpares.setStandardExchangeItem(cellValue);
                    case 5 -> productToSpares.setSpareDescription(cellValue);
                    case 6 -> productToSpares.setCatalogueVersion(cellValue);
                    case 7 -> productToSpares.setProductEndOfServiceDate(cellValue);
                    case 8 -> productToSpares.setLastUpdate(cellValue);
                    case 9 -> productToSpares.setAddedToCatalogue(cellValue);
                }
//                rowData.append(colCount + ")" + cellValue).append("\t");
                colCount++;
                if (colCount % 5000 == 0) {
                    System.out.println(colCount);
                }
            }
            // Print the row
            globalSparesRepository.insertProductToSpare(productToSpares);
//            System.out.println(rowData);
        }

        return true;
    }

    public static String getCellValueAsString(Cell cell) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Format the date to "yyyy-MM-dd HH:mm:ss"
                    Date date = cell.getDateCellValue();
                    return dateFormat.format(date);
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
