package com.L2.static_tools;

import com.L2.dto.PartDTO;

import java.util.List;


public class TableFormatter {

    public static String buildPartsTableString(String[] headers, List<PartDTO> rows) {
        int[] columnWidths = calculateColumnWidths(headers, rows);
        StringBuilder stringBuilder = new StringBuilder();

        // Build the headers
        for (int i = 0; i < headers.length; i++) {
            stringBuilder.append(String.format("%-" + columnWidths[i] + "s", headers[i]));
            if (i < headers.length - 1) stringBuilder.append(" | ");
        }
        stringBuilder.append("\r\n");

        // Build the separator line
        for (int i = 0; i < headers.length; i++) {
            stringBuilder.append("-".repeat(columnWidths[i]));
            if (i < headers.length - 1) stringBuilder.append("-+-");
        }
        stringBuilder.append("\r\n");

        // Build the rows
        for (PartDTO part : rows) {
            stringBuilder.append(String.format("%-" + columnWidths[0] + "s", part.getPartNumber()));
            stringBuilder.append(" | ");
            stringBuilder.append(String.format("%-" + columnWidths[1] + "s", part.getPartDescription()));
            stringBuilder.append(" | ");
            stringBuilder.append(String.format("%-" + columnWidths[2] + "s", part.getPartQuantity()));
            stringBuilder.append("\r\n");
        }

        return stringBuilder.toString();
    }

    private static int[] calculateColumnWidths(String[] headers, List<PartDTO> rows) {
        int[] columnWidths = new int[headers.length];
        // Calculate widths for the headers
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }

        // Calculate widths for the rows
        for (PartDTO part : rows) {
            columnWidths[0] = Math.max(columnWidths[0], part.getPartNumber().length());
            columnWidths[1] = Math.max(columnWidths[1], part.getPartDescription().length());
            columnWidths[2] = Math.max(columnWidths[2], part.getPartQuantity().length());
        }
        return columnWidths;
    }
}


