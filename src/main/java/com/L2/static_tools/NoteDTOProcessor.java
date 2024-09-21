package com.L2.static_tools;

import com.L2.dto.NoteDTO;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class NoteDTOProcessor {

    public static boolean isEmail(String issue) {
        // Check if the issue string contains the FSR identifying portions
        return issue.contains("FSR Request") && issue.contains("Team Name:");
    }

    public static NoteDTO processEmail(String email, int id) {
        // Split the input string into lines
        String[] lines = email.split("\n");

        // Initialize NoteDTO
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setIssue(email);
        noteDTO.setIsEmail(true);
        // this is important to save the same note
        noteDTO.setId(id);
        noteDTO.setTitle("FSR Request - 3Phase Power");
        noteDTO.setTimestamp(LocalDateTime.now());

        // Variables to store data
        String firstName = "";
        String lastName = "";
        String callInPerson = "";

        // Loop through lines and map fields
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("First Name:")) {
                firstName = line.replace("First Name:", "").trim();
            } else if (line.startsWith("Last Name:")) {
                lastName = line.replace("Last Name:", "").trim();
                callInPerson = firstName + " " + lastName;
                noteDTO.callInPersonProperty().set(callInPerson);
            } else if (line.startsWith("Site Name:")) {
                noteDTO.installedAtProperty().set(line.replace("Site Name:", "").trim());
            } else if (line.startsWith("Phone:")) {
                noteDTO.callInPhoneNumberProperty().set(line.replace("Phone:", "").trim());
            } else if (line.startsWith("Email:")) {
                noteDTO.callInEmailProperty().set(line.replace("Email:", "").trim());
            } else if (line.startsWith("WO:")) {
                noteDTO.workOrderProperty().set(line.replace("WO:", "").trim());
            } else if (line.startsWith("Name:")) {
                noteDTO.contactNameProperty().set(line.replace("Name:", "").trim());
            } else if (line.startsWith("Phone Number:")) {
                noteDTO.contactPhoneNumberProperty().set(line.replace("Phone Number:", "").trim());
            } else if (line.startsWith("Email:")) {
                noteDTO.contactEmailProperty().set(line.replace("Email:", "").trim());
            } else if (line.startsWith("Address:")) {
                noteDTO.streetProperty().set(lines[i + 1].trim());    // Mapping the full address to street for simplicity
                parseAddress(lines[i+2], noteDTO);
            } else if (line.startsWith("Affected Part Serial:")) {
                noteDTO.serialNumberProperty().set(line.replace("Affected Part Serial:", "").trim());
            }
        }
    return noteDTO;
    }

    public static void parseAddress(String address, NoteDTO noteDTO) {
        String city = "";
        String state = "";
        String zip = "";

        // Regular expression patterns
        Pattern zipPattern = Pattern.compile("\\d{5}(-\\d{4})?$");  // Matches ZIP code (5 digits, optional 4 digits after dash)
        Pattern statePattern = Pattern.compile("^[A-Z]{2}$");  // Matches state abbreviation (2 uppercase letters)

        // Split by commas or spaces
        String[] parts = address.split("[, ]+");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();

            // Check if part is a ZIP code
            if (zipPattern.matcher(part).matches()) {
                zip = part;
            }
            // Check if part is a state (two uppercase letters)
            else if (statePattern.matcher(part).matches()) {
                state = part;
            }
            // If not ZIP or state, assume it's part of the city
            else {
                if (!city.isEmpty()) {
                    city += " ";
                }
                city += part;
            }
        }

        // Trim extra spaces
        city = city.trim();

        // Output the result or set the fields in the DTO
        noteDTO.setCity(city);
        noteDTO.setState(state);
        noteDTO.setZip(zip);
    }
}


