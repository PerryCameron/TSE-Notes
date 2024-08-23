package com.L2.static_tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringChecker {

    public static String checkString(String type, String fieldValue) {
        return switch (type) {
            case "Call-in Phone" -> formatPhoneNumber(fieldValue);
            case "Work Order" -> formatWorkOrder(fieldValue);
            case "Case" -> formatCaseNumber(fieldValue);
            case "Call-in Email" -> formatEmail(fieldValue);
            default -> fieldValue;
        };
    }

    public static String formatEmail(String email)  {
        String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

        if (email == null || email.isEmpty()) {
            return "Invalid email address";
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            return "Invalid email address";
        }
        return email.toLowerCase();
    }

public static String formatCaseNumber(String input) {
        if (input == null) {
            return "Improper case number";
        }
        // Remove all non-digit characters
        String digits = input.replaceAll("\\D", "");
        // Check if the string has exactly 8 digits
        if (digits.length() == 9) {
            return digits;
        } else {
            return "Improper case number";
        }
    }


    public static String formatWorkOrder(String input) {
        if (input == null) {
            return "Improper Work Order";
        }
        // Remove all non-digit characters
        String digits = input.replaceAll("\\D", "");
        // Check if the length of the digits is exactly 8
        if (digits.length() == 8) {
            return "WO-" + digits;
        } else {
            return "Improper Work Order";
        }
    }


    public static String formatPhoneNumber(String phoneNumber) {
        System.out.println("formatting phone number");
        if (phoneNumber == null) {
            return("Phone number cannot be null");
        }
        // Remove all non-digit characters
        String digits = phoneNumber.replaceAll("\\D", "");
        // If the number has 11 digits and starts with '1', remove the first digit
        if (digits.length() == 11 && digits.startsWith("1")) {
            digits = digits.substring(1);
        }
        // Ensure the phone number has exactly 10 digits
        if (digits.length() != 10) {
            return("Invalid phone number");
        }
        // Format the number as (123)-456-7890
        return String.format("(%s)-%s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6, 10));
    }


}
