package com.L2.static_tools;

import com.L2.dto.NoteDTO;
import com.L2.dto.ResultDTO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
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
        NoteDTO noteDTO = new NoteDTO(id,true);
        noteDTO.setIssue(email);
        // this is important to save the same note
        noteDTO.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        noteDTO.setCaseNumber("");

        // Variables to store data
        String firstName = "";
        String lastName = "";
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
        String country = "";


        // Regular expression patterns
        Pattern zipPattern = Pattern.compile("\\d{5}(-\\d{4})?|[A-Z]\\d[A-Z] ?\\d[A-Z]\\d"); // US and Canadian ZIP/Postal codes
        Pattern statePattern = Pattern.compile("\\b[A-Z]{2}\\b"); // Matches state abbreviation (2 uppercase letters)
        Pattern countryPattern = Pattern.compile("\\b(Canada|United States|USA)\\b", Pattern.CASE_INSENSITIVE);


        // Split by lines and trim each line
        String[] lines = address.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();


            // If line contains a ZIP or Postal code, extract it
            Matcher zipMatcher = zipPattern.matcher(line);
            if (zipMatcher.find()) {
                zip = zipMatcher.group();
                // Try to capture the city, state, and ZIP in one line (e.g., "City, State 12345")
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length; i++) {
                    if (zip.equals(parts[i])) {
                        if (i > 0) {
                            state = parts[i - 1]; // State should be before ZIP
                        }
                        if (i > 1) {
                            city = parts[i - 2]; // City should be before state
                        }
                    }
                }
                continue; // Move to the next line after capturing the ZIP
            }


            // If line contains a state, capture it and check if the previous part is a city
            Matcher stateMatcher = statePattern.matcher(line);
            if (stateMatcher.find()) {
                state = stateMatcher.group();
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length; i++) {
                    if (state.equals(parts[i]) && i > 0) {
                        city = parts[i - 1]; // City is usually right before the state
                    }
                }
                continue; // Move to the next line after capturing the state
            }


            // Check if line contains a country
            Matcher countryMatcher = countryPattern.matcher(line);
            if (countryMatcher.find()) {
                country = countryMatcher.group();
                continue; // Move to the next line after capturing the country
            }


            // If none of the above, assume this line is part of the city or street address
            if (!line.isEmpty()) {
                if (!city.isEmpty()) {
                    city += " ";
                }
                city += line;
            }
        }


        // Trim extra spaces and set the results in the DTO
        city = city.trim();
        noteDTO.setCity(city);
        noteDTO.setState(state);
        noteDTO.setZip(zip);
        noteDTO.setCountry(country); // Assuming NoteDTO has a country field
    }

}


