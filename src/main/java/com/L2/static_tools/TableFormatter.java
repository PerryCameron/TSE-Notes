package com.L2.static_tools;

import com.L2.dto.PartDTO;

import java.util.List;


public class TableFormatter {

    public static String buildPartsTableString(List<PartDTO> rows, String orderNumber) {
        StringBuilder stringBuilder = new StringBuilder();

        if (orderNumber != null && !orderNumber.isEmpty()) {
            stringBuilder.append("Part Order: ").append(orderNumber).append("\r\n");
        }


        // Build the rows
        for (PartDTO part : rows) {
            stringBuilder.append(String.format("%-15s", part.getPartNumber()));
            stringBuilder.append("   ");
            stringBuilder.append(String.format("%-" + (80 - 15 - 10) + "s", part.getPartDescription()));
            stringBuilder.append("   ");
            stringBuilder.append("Qty. ").append(part.getPartQuantity());
            stringBuilder.append("\r\n");
        }

        return stringBuilder.toString();
    }
}


