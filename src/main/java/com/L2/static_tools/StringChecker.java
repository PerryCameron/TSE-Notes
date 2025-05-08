package com.L2.static_tools;

import com.L2.dto.ResultDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringChecker {

    public static ResultDTO checkString(String type, String fieldValue) {
        return switch (type) {
            case "Call-in Phone" -> formatPhoneNumber(fieldValue);
            case "Work Order" -> formatWorkOrder(fieldValue);
            case "Case" -> formatCaseNumber(fieldValue);
            case "Call-in Email" -> formatEmail(fieldValue);
            case "Contact Name" -> formatName(fieldValue);
            default -> new ResultDTO(fieldValue, Boolean.TRUE);
        };
    }

    public static ResultDTO formatEmail(String email) {
        // Updated EMAIL_REGEX to handle multiple '.' in the domain part
        String EMAIL_REGEX = "^[\\w-.]+@[\\w-]+(\\.[\\w-]+)+$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        ResultDTO resultDTO = new ResultDTO();

        if (email == null) {
            resultDTO.setFieldName("Null email");
            resultDTO.setSuccess(Boolean.FALSE);
            return resultDTO;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            resultDTO.setSuccess(Boolean.FALSE);
        } else {
            resultDTO.setSuccess(Boolean.TRUE);
        }

        resultDTO.setFieldName(email.toLowerCase());
        return resultDTO;
    }


    public static ResultDTO formatCaseNumber(String input) {
        ResultDTO resultDTO = new ResultDTO();
        if (input == null) {
            resultDTO.setFieldName("Null Case Number");
            resultDTO.setSuccess(Boolean.FALSE);
            return resultDTO;
        }
        // Remove all non-digit characters
        String digits = input.replaceAll("\\D", "");
        // Check if the string has exactly 9 digits
        if (digits.length() == 9) {
            resultDTO.setSuccess(Boolean.TRUE);
        } else {
            resultDTO.setSuccess(Boolean.FALSE);
        }
        resultDTO.setFieldName(digits);
        return resultDTO;
    }


    public static ResultDTO formatWorkOrder(String input) {
        ResultDTO resultDTO = new ResultDTO();
        if (input == null) {
            resultDTO.setFieldName("Null Work Order");
            resultDTO.setSuccess(Boolean.FALSE);
            return resultDTO;
        }
        // Remove all non-digit characters
        String digits = input.replaceAll("\\D", "");
        // Check if the length of the digits is exactly 8
        if (digits.length() == 8) {
            resultDTO.setFieldName("WO-" + digits);
            resultDTO.setSuccess(Boolean.TRUE);
        }
        else {
            resultDTO.setFieldName(input);
            resultDTO.setSuccess(Boolean.FALSE);
        }

        return resultDTO;
    }


    public static ResultDTO formatPhoneNumber(String phoneNumber) {
        ResultDTO resultDTO = new ResultDTO();
        if (phoneNumber == null) {
            resultDTO.setFieldName("Null Phone Number");
            resultDTO.setSuccess(Boolean.FALSE);
            return resultDTO;
        }
        // Remove all non-digit characters
        String digits = phoneNumber.replaceAll("\\D", "");
        // If the number has 11 digits and starts with '1', remove the first digit
        if (digits.length() == 11 && digits.startsWith("1")) {
            digits = digits.substring(1);
        }
        // Ensure the phone number has exactly 10 digits
        if (digits.length() != 10) {
            resultDTO.setFieldName(phoneNumber);
            resultDTO.setSuccess(Boolean.FALSE);
            return resultDTO;
        } else resultDTO.setSuccess(Boolean.TRUE);
        // Format the number as (123)-456-7890
        resultDTO.setFieldName(String.format("(%s)-%s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6, 10)));
        return resultDTO;
    }

    public static ResultDTO formatName(String input) {
        ResultDTO result = new ResultDTO();
        if (input == null || input.isEmpty()) {
            result.setFieldName(input);
            result.setSuccess(false);
            return result;
        }
        // Trim leading and trailing whitespace
        input = input.trim();
        // Split name into words
        String[] words = input.split("\\s+");
        StringBuilder formattedName = new StringBuilder();
        // Regular expression for Irish/Scottish "Mac" and "Mc" names
        Pattern macPattern = Pattern.compile("^(Mac|Mc)([A-Z][a-z]+)$");
        for (String word : words) {
            if (word.length() > 2 && (word.startsWith("Mac") || word.startsWith("Mc"))) {
                // Handle names starting with "Mac" or "Mc"
                Matcher matcher = macPattern.matcher(word);
                if (matcher.matches()) {
                    // Capitalize correctly for names like "MacGregor", "McEwan", etc.
                    formattedName.append(matcher.group(1)); // Mac or Mc
                    formattedName.append(matcher.group(2)); // Capitalize rest
                } else {
                    formattedName.append(capitalizeFirstLetter(word)); // Fallback capitalization
                }
            } else {
                // Capitalize the first letter of any other word
                formattedName.append(capitalizeFirstLetter(word));
            }
            formattedName.append(" ");
        }
        // Remove the trailing space
        String finalName = formattedName.toString().trim();
        // Set the results
        result.setFieldName(finalName);
        result.setSuccess(true);
        return result;
    }

    // Helper method to capitalize the first letter of each word
    private static String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static boolean hasNumbers(String input) {
        return input != null && input.matches(".*\\d+.*");
    }

}
