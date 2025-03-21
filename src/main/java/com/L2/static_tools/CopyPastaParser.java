package com.L2.static_tools;

import static com.L2.static_tools.StateCodes.STATE_ABBREVIATIONS;

public class CopyPastaParser {

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

    public static String[] parseAddress() {
        String clipboardText = ClipboardUtils.getClipboardText();  // Method to get text from clipboard
        String[] lines = clipboardText.split("\\n");
        String[] parsedInfo = new String[6];
        String company = "";
        String streetAddress = "";
        String city = "";
        String state = "";
        String zipCode = "";
        String country = "";

        // Check if the second line contains "Account" or "Account Owner"
        if (lines.length > 1 && (lines[1].equalsIgnoreCase("Account") || lines[1].equalsIgnoreCase("Account Owner"))) {
            // Use different logic depending on the content of the second line
            if (lines[1].equalsIgnoreCase("Account Owner")) {
                // Parsing logic for when the second line contains "Account Owner"
                company = lines[0];  // First line is the company name
                streetAddress = lines[4];  // Line after "Address"
                String cityStateZip = lines[5];
                country = lines[6];

                // Parse city, state, and ZIP code from "Raleigh, North Carolina 27607-0000"
                String[] addressParts = cityStateZip.split(",");
                if (addressParts.length >= 2) {
                    city = addressParts[0].trim();
                    String[] stateAndZip = addressParts[1].trim().split(" ");
                    state = stateAndZip[0];
                    zipCode = stateAndZip.length > 1 ? stateAndZip[1] : "";
                }
            } else if (lines[1].equalsIgnoreCase("Account")) {
                // Parsing logic for when the second line contains "Account"
                company = lines[0];  // First line is the company name
                streetAddress = lines[7];  // Line after "Address"
                String cityStateZip = lines[8];
                String stateCountry = lines[9];

                // Parse city, state, and ZIP code from "27607-0000, Raleigh"
                String[] cityZipParts = cityStateZip.split(",");
                if (cityZipParts.length >= 2) {
                    zipCode = cityZipParts[0].trim();
                    city = cityZipParts[1].trim();
                }

                // Parse state and country from "North Carolina, USA"
                String[] stateCountryParts = stateCountry.split(",");
                if (stateCountryParts.length >= 2) {
                    state = stateCountryParts[0].trim();
                    country = stateCountryParts[1].trim();
                }
            }
        }

        // Populate the parsedInfo array
        parsedInfo[0] = company;
        parsedInfo[1] = streetAddress;
        parsedInfo[2] = city;
        parsedInfo[3] = state;
        parsedInfo[4] = zipCode;
        parsedInfo[5] = country;

        return parsedInfo;
    }

    // Method to abbreviate state/province names
    public static String abbreviateState(String state) {
        return STATE_ABBREVIATIONS.getOrDefault(state, state);
    }
}
