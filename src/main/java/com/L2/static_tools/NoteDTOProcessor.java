package com.L2.static_tools;

import com.L2.dto.NoteDTO;
import com.L2.dto.ResultDTO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class NoteDTOProcessor {

    public static boolean isEmail(String issue) {
        // Check if the issue string contains the FSR identifying portions
        return issue.contains("FSR Request") && issue.contains("Team Name:");
    }

    public static NoteDTO processEmail(String email, int id) {
        // Split the input string into lines
        String[] lines = email.split("\n");

        // Initialize NoteDTO
        NoteDTO noteDTO = new NoteDTO(id,true);
        noteDTO.setIssue(email);
        // this is important to save the same note
        noteDTO.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        noteDTO.setCaseNumber("");

        // Variables to store data
        String firstName = "";
        String lastName;
        String previousLine = "";

        // Loop through lines and map fields
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if(i > 0) {
                previousLine = lines[i - 1].trim();
            }
            if (line.startsWith("First Name:")) {
                firstName = line.replace("First Name:", "").trim();
            } else if (line.startsWith("Last Name:")) {
                lastName = line.replace("Last Name:", "").trim();
                ResultDTO resultDTO = StringChecker.formatName(firstName + " " + lastName);
                noteDTO.callInPersonProperty().set(resultDTO.getFieldName());
            } else if (line.startsWith("Site Name:")) {
                noteDTO.installedAtProperty().set(line.replace("Site Name:", "").trim());
            } else if (line.startsWith("Team Name:")) {
                noteDTO.setTitle(line.replace("Team Name:", "").trim());
            } else if (line.startsWith("Phone:")) {
                ResultDTO resultDTO = StringChecker.formatPhoneNumber(line.replace("Phone:", "").trim());
                noteDTO.callInPhoneNumberProperty().set(resultDTO.getFieldName());
            } else if (line.startsWith("Email:")) {
                ResultDTO resultDTO = StringChecker.formatEmail(line.replace("Email:", "").trim());
                if(previousLine.startsWith("Phone:")) {
                    noteDTO.callInEmailProperty().set(resultDTO.getFieldName());
                } else {
                    noteDTO.contactEmailProperty().set(resultDTO.getFieldName());
                }
            } else if (line.startsWith("WO:")) {
                ResultDTO resultDTO = StringChecker.formatWorkOrder(line.replace("WO:", "").trim());
                noteDTO.workOrderProperty().set(resultDTO.getFieldName());
            } else if (line.startsWith("Name:")) {
                ResultDTO resultDTO = StringChecker.formatName(line.replace("Name:", "").trim());
                noteDTO.contactNameProperty().set(resultDTO.getFieldName());
            } else if (line.startsWith("Phone Number:")) {
                ResultDTO resultDTO = StringChecker.formatPhoneNumber(line.replace("Phone Number:", "").trim());
                noteDTO.contactPhoneNumberProperty().set(resultDTO.getFieldName());
            } else if (line.startsWith("Affected Part Serial:")) {
                noteDTO.serialNumberProperty().set(line.replace("Affected Part Serial:", "").trim());
            }
        }

        Map<String, String> address = AddressParser.extractAddress(email);
        noteDTO.streetProperty().set(address.get("Street"));
        noteDTO.cityProperty().set(address.get("City"));
        noteDTO.stateProperty().set(address.get("State"));
        noteDTO.zipProperty().set(address.get("Zip"));
        noteDTO.countryProperty().set(address.get("Country"));
    return noteDTO;
    }


//    private static void parseAddress(String[] lines, int index, NoteDTO noteDTO) {
//        String streetLine = lines[index + 1].trim(); // Assuming the next line is part of the address
//        String remainingLine = index + 2 < lines.length ? lines[index + 2].trim() : "";
//
//
//        String street = streetLine;
//        String city = "";
//        String state = "";
//        String postalCode = "";
//        String country = "USA"; // Default country
//
//
//        // Check for a postal code in the second line
//        if (!remainingLine.isEmpty()) {
//            String[] parts = remainingLine.split("\\s+");
//            for (String part : parts) {
//                if (part.matches("\\d{5}(-\\d{4})?") || part.matches("[A-Z]\\d[A-Z] \\d[A-Z]\\d")) {
//                    postalCode = part; // Matches US or Canadian postal codes
//                } else if (StateCodes.STATE_ABBREVIATIONS.containsKey(part) || StateCodes.STATE_ABBREVIATIONS.containsValue(part)) {
//                    state = part; // Match US state or Canadian province
//                }
//            }
//            if (remainingLine.contains("Canada")) {
//                country = "Canada";
//            }
//        }
//
//
//        // If the street line contains city and state
//        String[] streetParts = streetLine.split("\\s+");
//        for (int i = streetParts.length - 1; i >= 0; i--) {
//            String part = streetParts[i];
//            if (StateCodes.STATE_ABBREVIATIONS.containsKey(part) || StateCodes.STATE_ABBREVIATIONS.containsValue(part)) {
//                state = part;
//                street = streetLine.substring(0, streetLine.indexOf(part)).trim();
//                break;
//            }
//        }
//
//
//        // Set parsed fields to the NoteDTO
//        noteDTO.streetProperty().set(street);
//        noteDTO.cityProperty().set(city);
//        noteDTO.stateProperty().set(state);
//        noteDTO.zipProperty().set(postalCode);
//        noteDTO.countryProperty().set(country);
//    }
}


