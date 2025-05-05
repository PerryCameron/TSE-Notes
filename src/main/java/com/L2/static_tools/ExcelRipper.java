package com.L2.static_tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.PropertiesDTO;
import com.L2.dto.global_spares.ReplacementCrDTO;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.repository.interfaces.GlobalSparesRepository;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelRipper {
    private static final Logger logger = LoggerFactory.getLogger(ExcelRipper.class);

    public static boolean extractWorkbookToSql(XSSFWorkbook workbook) {
        GlobalSparesRepository globalSparesRepository = new GlobalSparesRepositoryImpl();
        Sheet sheet = workbook.getSheet("Product to Spares");
        if (sheet == null) {
            System.out.println("Sheet 'Product to Spares' not found.");
            return false;
        }
        // extracts metadate from workbook
        logger.info("Saving Meta data properties");
        extractWorkbookProperties(workbook, globalSparesRepository);
        ProductToSparesDTO productToSpares = new ProductToSparesDTO(false, false);
        logger.info("Ripping Product to Spares");
        extractProductToSpares(sheet, productToSpares, globalSparesRepository, false);
        productToSpares.setArchived(true);
        sheet = workbook.getSheet("Archived Product to Spares");
        logger.info("Ripping Archived Product to Spares");
        extractProductToSpares(sheet, productToSpares, globalSparesRepository, true);
        ReplacementCrDTO replacementCrDTO = new ReplacementCrDTO();
        sheet = workbook.getSheet("Replacement CRs");
        logger.info("Ripping Replacement CRs (3-ph)");
        extractReplacementCr(sheet, replacementCrDTO, globalSparesRepository);
        sheet = workbook.getSheet("Uniflair Cross Reference");
        logger.info("Ripping Replacement CRs (Uniflair Cross Reference)");
        extractReplacementCr(sheet, replacementCrDTO, globalSparesRepository);
        logger.info("Consolidating Product to Spares ");
        consolidateWithJSON(false, globalSparesRepository);
        logger.info("Consolidating Archived Product to Spares");
        return true;
    }

    // I would like this method to make my JSON
    private static void consolidateWithJSON(boolean isArchived, GlobalSparesRepository globalSparesRepository) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> compactedSpares = globalSparesRepository.getDistinctSpareItems(isArchived);

        compactedSpares.forEach(spare -> {

            // Get pim_range values for this spare_item
            List<String> ranges = globalSparesRepository.getRangesFromSpareItem(spare, isArchived);

            // Build JSON for pim column
            List<Map<String, Object>> pimData = new ArrayList<>();
            for (String range : ranges) {
                List<String> products = globalSparesRepository.getProductsFromRange(spare, range, isArchived);
                if (!products.isEmpty()) {
                    Map<String, Object> rangeEntry = new HashMap<>();
                    rangeEntry.put("range", range);
                    rangeEntry.put("product_families", products);
                    pimData.add(rangeEntry);
                }
            }

            // Skip if no pim data
            if (pimData.isEmpty()) {
                logger.warn("No pim data for spare_item: {}", spare);
                return;
            }

            // Convert pimData to JSON
            String pimJson;
            try {
                pimJson = objectMapper.writeValueAsString(pimData);
            } catch (Exception e) {
                logger.error("Error serializing JSON for spare_item: {}", spare, e);
                return;
            }
            System.out.println(pimJson);

        });
    }

    public static boolean extractWorkbookProperties(XSSFWorkbook workbook, GlobalSparesRepository globalSparesRepository) {
        PropertiesDTO propertiesDTO = new PropertiesDTO();
        try {
            POIXMLProperties properties = workbook.getProperties();
            POIXMLProperties.CoreProperties coreProperties = properties.getCoreProperties();
            propertiesDTO.setLastModifiedDate(coreProperties.getModified().toString());
            propertiesDTO.setLastModifiedBy(coreProperties.getLastModifiedByUser());
            propertiesDTO.setCreatedBy(coreProperties.getCreator());
            propertiesDTO.setCreationDate(coreProperties.getCreated().toString());
            globalSparesRepository.insertWorkbookProperties(propertiesDTO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void extractReplacementCr(Sheet sheet, ReplacementCrDTO replacementCrDTO, GlobalSparesRepository globalSparesRepository) {
        // Iterate through the first 10 rows
        for (Row row : sheet) {
            // this is temp for testing
            if (row.getRowNum() >= 300) {
                break; // Stop after 300 rows
            }
            // we will not start writing until we get to row three
            if (row.getRowNum() < 3) {
                continue;
            }
            // start rowCount when we begin

            int colCount = 0;
            for (Cell cell : row) {
                String cellValue = getCellValueAsString(cell);
                switch (colCount) {
                    case 0 -> replacementCrDTO.setItem(cellValue);
                    case 1 -> replacementCrDTO.setReplacement(cellValue);
                    case 2 -> replacementCrDTO.setComment(cellValue);
                    case 3 -> {
                        try {
                            if (!cellValue.isEmpty())
                                replacementCrDTO.setOld_qty(Double.valueOf(cellValue));
                            else
                                replacementCrDTO.setOld_qty(0);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            e.printStackTrace();
                            replacementCrDTO.setOld_qty(99999);
                        }
                    }
                    case 4 -> {
                        try {
                            if (!cellValue.isEmpty())
                                replacementCrDTO.setNew_qty(Double.valueOf(cellValue));
                            else
                                replacementCrDTO.setNew_qty(0);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            e.printStackTrace();
                            replacementCrDTO.setNew_qty(99999);
                        }
                    }
                }
                colCount++;
                if (row.getRowNum() % 100 == 0 && colCount == 1) {
                    logger.info("{} ", row.getRowNum());
                }
            }
            // Print the row
            if (!replacementCrDTO.getItem().isEmpty())
                globalSparesRepository.insertReplacementCr(replacementCrDTO);
            replacementCrDTO.clear();
        }
    }

    private static void extractProductToSpares(Sheet sheet, ProductToSparesDTO productToSpares, GlobalSparesRepository globalSparesRepository, boolean isArchived) {
        // Iterate through the first 10 rows
        for (Row row : sheet) {
            // this is temp for testing
            if (row.getRowNum() >= 300) {
                break; // Stop after 300 rows
            }
            // we will not start writing until we get to row three
            if (row.getRowNum() < 3) {
                continue;
            }
            // start rowCount when we begin

            int colCount = 0;
            for (Cell cell : row) {
                String cellValue = getCellValueAsString(cell);
                switch (colCount) {
                    case 0 -> {
                        productToSpares.setPimRange(cellValue);
                        productToSpares.setArchived(isArchived);
                        productToSpares.setCustomAdd(false);
                    }
                    case 1 -> productToSpares.setPimProductFamily(cellValue);
                    case 2 -> productToSpares.setSpareItem(cellValue);
                    case 3 -> productToSpares.setReplacementItem(cellValue);
                    case 4 -> productToSpares.setStandardExchangeItem(cellValue);
                    case 5 -> productToSpares.setSpareDescription(cellValue);
                    case 6 -> productToSpares.setCatalogueVersion(cellValue);
                    case 7 -> productToSpares.setProductEndOfServiceDate(cellValue);
                    case 8 -> {
                        if (isArchived)
                            productToSpares.setRemovedFromCatalogue(cellValue);
                        else
                            productToSpares.setLastUpdate(cellValue);
                    }
                    case 9 -> {
                        if (isArchived)
                            productToSpares.setComments(cellValue);
                        else
                            productToSpares.setAddedToCatalogue(cellValue);
                    }
                    case 10 -> {
                        if (!isArchived)
                            if(cellValue.isEmpty())
                                productToSpares.setComments(null);
                            else
                            productToSpares.setComments(cellValue);
                    }
                }
                if (row.getRowNum() < 5)
                    System.out.println(colCount + ") " + cellValue);
                colCount++;
                if (row.getRowNum() % 100 == 0 && colCount == 1) {
                    logger.info("{} ", row.getRowNum());
                }
            }
            // Print the row
            globalSparesRepository.insertProductToSpare(productToSpares);
            productToSpares.clear();
        }
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
