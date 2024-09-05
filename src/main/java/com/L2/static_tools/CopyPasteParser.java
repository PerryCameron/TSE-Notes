package com.L2.static_tools;

import java.util.HashMap;

import static com.L2.static_tools.StateCodes.STATE_ABBREVIATIONS;

public class CopyPasteParser {




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
            if (line.contains("Work Phone") && i + 1 < lines.length) {
                workPhone = lines[i + 1].replace("Click to dial", "").trim();
            }
            // Mobile phone
            if (line.contains("Mobile") && i + 1 < lines.length) {
                mobilePhone = lines[i + 1].replace("Click to dial", "").trim();
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

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Company name is assumed to be the first line
            if (i == 0) {
                company = line;
            }

            // Street address is assumed to be on the line after "Address"
            if (line.equalsIgnoreCase("Address") && i + 1 < lines.length) {
                streetAddress = lines[i + 1].trim();
            }

            // Parse city, state, and ZIP code (e.g., "Kearneysville, West Virginia 25430-5200")
            if (i > 0 && line.contains(",") && line.matches(".*\\d{5}.*")) {
                String[] addressParts = line.split(",");
                if (addressParts.length >= 2) {
                    city = addressParts[0].trim();
                    String stateAndZip = addressParts[1].trim();

                    // Handle multi-word states (e.g., "West Virginia")
                    String[] stateAndZipParts = stateAndZip.split(" ");
                    if (stateAndZipParts.length >= 2) {
                        state = stateAndZipParts[0] + " " + stateAndZipParts[1]; // Combine state words
                        zipCode = stateAndZipParts[2]; // Extract zip code
                    } else {
                        state = stateAndZipParts[0]; // Single-word state
                        zipCode = stateAndZipParts[1]; // Zip code
                    }
                }
            }

            // Country is assumed to be the last line
            if (line.equalsIgnoreCase("USA") || line.equalsIgnoreCase("Canada")) {
                country = line;
            }
        }

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
