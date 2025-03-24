package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyPastaParser {

    private static final Logger logger = LoggerFactory.getLogger(CopyPastaParser.class);


    public static String[] extractContactInfo() {
        String text = ClipboardUtils.getClipboardText();
        String[] lines = text.split("\\n");
        String[] contactInfo = new String[3];
        String name = "";
        String workPhone = "";
        String mobilePhone = "";
        String email = "";

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            // Name is assumed to be the first line
            if (i == 0 && !line.isEmpty()) {
                name = line;
            }
            // Work phone
            if (line.contains("Work Phone")) {
                // Check if the next line contains "Click to dial" with a phone number
                if (i + 1 < lines.length && lines[i + 1].contains("Click to dial")) {
                    workPhone = lines[i + 1].replace("Click to dial", "").trim();
                }
            }
            // Mobile phone
            if (line.contains("Mobile")) {
                // Check if the next line contains "Click to dial" with a phone number
                if (i + 1 < lines.length && lines[i + 1].contains("Click to dial")) {
                    mobilePhone = lines[i + 1].replace("Click to dial", "").trim();
                }
            }
            // Email
            if (line.contains("@")) {
                email = line.trim();
            }
        }
        // Populate contactInfo array
        contactInfo[0] = name;
        contactInfo[1] = !workPhone.isEmpty() ? workPhone : (!mobilePhone.isEmpty() ? mobilePhone : "");
        contactInfo[2] = !email.isEmpty() ? email : "";
        return contactInfo;
    }

    private enum Marker {
        none,
        account,
        address,
        zip,
        country
    }

    public static String[] parseAddress() {
        String clipboardText = ClipboardUtils.getClipboardText();  // Method to get text from clipboard
        String[] lines = clipboardText.split("\\n");
        String[] parsedInfo = new String[6];

        // If we have no data return no data
        if (lines.length == 0) {
            return new String[6];
        }

        // Trim lines to avoid whitespace issues
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }

        boolean foundType = false;
        for (String line : lines) {
            if (line.equalsIgnoreCase("Account Owner")) {
                parsedInfo = parseType2(lines);
                foundType = true;
                break;
            }
            if (lines[0].toUpperCase().endsWith("SITE")) {
                parsedInfo = parseType1(lines);
                foundType = true;
                break;
            }
        }
        // if we don't find a type lets leave a blank address
        if (!foundType) {
            parsedInfo = new String[6];
            for (int i = 0; i < parsedInfo.length; i++) {
                parsedInfo[i] = "???";  // Directly set array elements
            }
        }
        return parsedInfo;
    }

    private static String[] parseType1(String[] lines) {
        Marker marker = Marker.none;
        String[] parsedInfo = new String[6];
        for (String line : lines) {
            switch (marker) {
                case country -> { // order matters here
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        parsedInfo[3] = parts[0].trim(); // the state
                        parsedInfo[5] = parts[1].trim(); // the country
                    }
                    marker = Marker.none;
                }
                case zip -> { // order matters here
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        parsedInfo[4] = parts[0].trim();  // the zip code
                        parsedInfo[2] = parts[1].trim(); // the city
                    }
                    marker = Marker.country;
                }
                case account -> {
                    parsedInfo[0] = line;
                    marker = Marker.none;
                }
                case address -> {
                    parsedInfo[1] = line;
                    marker = Marker.zip;
                }
            }
            switch (line) {
                case "Account" -> marker = Marker.account;
                case "Address" -> marker = Marker.address;
            }
        }
        return parsedInfo;
    }

    private static String[] parseType2(String[] lines) {
        Marker marker = Marker.account;
        String[] parsedInfo = new String[6];
        for (String line : lines) {
            switch (marker) {
                case account -> {
                    parsedInfo[0] = line;
                    marker = Marker.none;
                }
                case zip -> {
                    // Muscatine, Iowa 52761-3730 <- for USA
                    // Brampton, Ontario L6T 4B8 <- for Canada
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        parsedInfo[2] = parts[0].trim(); // City (Muscatine or Brampton)
                        String stateZipPart = parts[1].trim(); // "Iowa 52761-3730" or "Ontario L6T 4B8"
                        String[] stateZipWords = stateZipPart.split("\\s+");
                        if (stateZipWords.length >= 2) {
                            // Check if the last part looks like a Canadian postal code (e.g., "L6T 4B8")
                            String lastPart = stateZipWords[stateZipWords.length - 1];
                            String secondLastPart = stateZipWords[stateZipWords.length - 2];
                            if (lastPart.matches("\\d[A-Za-z]\\d") && secondLastPart.matches("[A-Za-z]\\d[A-Za-z]")) {
                                // Canadian format: "Ontario L6T 4B8"
                                parsedInfo[4] = secondLastPart + " " + lastPart; // ZipCode (L6T 4B8)
                                parsedInfo[3] = stateZipPart.substring(0, stateZipPart.lastIndexOf(secondLastPart)).trim(); // State (Ontario)
                            } else {
                                // USA format: "Iowa 52761-3730"
                                int lastSpaceIndex = stateZipPart.lastIndexOf(" ");
                                if (lastSpaceIndex != -1) {
                                    parsedInfo[3] = stateZipPart.substring(0, lastSpaceIndex).trim(); // State (Iowa)
                                    parsedInfo[4] = stateZipPart.substring(lastSpaceIndex + 1).trim(); // ZipCode (52761-3730)
                                }
                            }
                        } else {
                            // Fallback: assume state/province only (unlikely, but safe)
                            parsedInfo[3] = stateZipPart;
                        }
                    }
                    marker = Marker.country; // Assuming this follows your flow
                }
                case address -> {
                    parsedInfo[1] = line;
                    marker = Marker.zip;
                }
            }
            switch (line) {
                case "Address" -> marker = Marker.address;
                case "Canada" -> parsedInfo[5] = "Canada";
                case "USA" -> parsedInfo[5] = "USA";
            }
        }
        return parsedInfo;
    }
}

//        parsedInfo[0] = company;
//        parsedInfo[1] = streetAddress;
//        parsedInfo[2] = city;
//        parsedInfo[3] = state;
//        parsedInfo[4] = zipCode;
//        parsedInfo[5] = country;