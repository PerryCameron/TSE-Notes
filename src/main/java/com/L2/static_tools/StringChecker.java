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
}
