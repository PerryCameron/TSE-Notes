package com.L2.static_tools;

import com.L2.dto.NoteFx;
import com.L2.dto.ResultDTO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class NoteDTOProcessor {

    public static boolean isEmail(String issue) {
        // Check if the issue string contains the FSR identifying portions
        return issue.contains("FSR Request") && issue.contains("Team Name:");
    }

    public static NoteFx processEmail(String email, int id) {
        // Split the input string into lines
        String[] lines = email.split("\n");

        // Initialize NoteDTO
        NoteFx noteDTO = new NoteFx(id,true);
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
}


