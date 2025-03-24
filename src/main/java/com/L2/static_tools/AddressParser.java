package com.L2.static_tools;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.*;


public class AddressParser {

    private static final Logger logger = LoggerFactory.getLogger(AddressParser.class);
    // List of common street types
    private static final List<String> STREET_TYPES = Arrays.asList(
            "St", "Street", "Ave", "Avenue", "Rd", "Road", "Blvd", "Boulevard", "Ln", "Lane", "Dr", "Drive", "Ct", "Court", "Pl", "Place", "Terrace", "Way", "Circle", "Cir"
    );
    private static final List<String> UNIT_DESIGNATORS = Arrays.asList(
            "Apt", "Suite", "Ste", "Unit", "Fl", "Floor", "Rm", "Room", "Bldg", "Building"
    );

    // Regex for Zips
    private static final Pattern US_ZIP_PATTERN = Pattern.compile("\\d{5}(-\\d{4})?");
    private static final Pattern CANADA_POSTAL_PATTERN = Pattern.compile("[A-Z]\\d[A-Z] \\d[A-Z]\\d");

    // Extract address between "Address:" and "Order Details"
    public static Map<String, String> extractAddress(String email) {
        String addressBlock = extractAddressBlock(email);
        if (addressBlock == null || addressBlock.isEmpty()) {
            throw new IllegalArgumentException("No address block found");
        }
        return parseAddress(addressBlock, null, null);
    }

    // Extract text between "Address:" and "Order Details"
    public static String extractAddressBlock(String email) {
        int startIndex = email.indexOf("Address:");
        int endIndex = email.indexOf("Order Details");
        if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
            return null;
        }
        return email.substring(startIndex + "Address:".length(), endIndex).trim();
    }

    private static Map<String, String> parseAddress(String unFormattedAddress,
                                                    Map<String, String> addressComponents,
                                                    Map<String, MatchedRange> componentLocations) {
        String addressBlock = removeDots(unFormattedAddress);
        int numberOfCommas = countCommas(addressBlock);
        int numberOfLines = countLines(addressBlock);
        int numberOfStreetTypes = containsStreetType(addressBlock);
        logger.info("Number of street types: {}", numberOfStreetTypes);
        boolean firstWordIsInteger = isFirstWordInteger(addressBlock);
        boolean containsUnit = containsUnitDesignator(addressBlock);
        // If maps are null, initialize them
        if (addressComponents == null) {
            addressComponents = new HashMap<>();
        }
        if (componentLocations == null) {
            componentLocations = new HashMap<>();
        }


        // does this start with street address
        if(firstWordIsInteger) {
            // we have Rd, Lane, Circle etc. in string
            if(numberOfStreetTypes > 0) {
                MatchedRange streetType = getLastStreetTypeRange(addressBlock);
                if(streetType != null) {
                    componentLocations.put("streetType", streetType);
                    addressComponents.put("Street", capitalizeWords(addressBlock.substring(0, streetType.getEnd())));
                } else {
                    logger.warn("Street type is null");
                }
            } else {
                if(numberOfCommas > 1) { // possibly a french address seperated by commas?
                    logger.info("There are lots of commas, possibly a french address");
                    String[] getCommaSeperatedAddressComponents = addressBlock.split(",");
                    if (getCommaSeperatedAddressComponents[0] != null)
                        addressComponents.put("Street", capitalizeWords(getCommaSeperatedAddressComponents[0].trim()));
                    if (getCommaSeperatedAddressComponents[1] != null)
                        addressComponents.put("City", getCommaSeperatedAddressComponents[1].trim());
                }
                // there are 0 street types
            }
        } else { // First word is not an integer
            if(numberOfLines > 1) {
                String newAddressBlock = processAndCombineLines(addressBlock);
                logger.info("Recursion with modified block: {}", newAddressBlock);
                // recursion with chopped addressBlock is being called here
                parseAddress(newAddressBlock, addressComponents, componentLocations);
            }
        }
        // get the state/province and determine country
        MatchedRange stateOrProvence = parseAddressForLocation(addressBlock);
        if(stateOrProvence != null) {
            // what country did our state / province indicate?
            componentLocations.put("state", stateOrProvence);
            if(stateOrProvence.getType().equals("USA")) {
                MatchedRange usZip = findUSZipCode(addressBlock);
                if(usZip != null) {
                    componentLocations.put("postalCode", usZip);
                }
            } else if(stateOrProvence.getType().equals("Canada")) {
                MatchedRange caZip = findCanadaZipCode(addressBlock);
                if(caZip != null) {
                    componentLocations.put("postalCode", caZip);
                }
            } else {
                componentLocations.put("postalCode", new MatchedRange(0, 0, "unknown"));
            }
            // Get the city
            if(componentLocations.get("postalCode").getStart() > 0) {
                if(componentLocations.get("streetType") != null) {
                    String city = addressBlock.substring(componentLocations.get("streetType").getEnd() + 1,
                            componentLocations.get("state").getStart() - 1);
                    // has apt, ste, rm etc in string
                    if(containsUnit) {
                        String[] unitAndCity = extractUnitAndRemainingText(city.trim());
                        if(unitAndCity != null) {
                            String newStreet = addressComponents.get("Street") + ", " + unitAndCity[0] + " " + unitAndCity[1];
                            addressComponents.put("Street", capitalizeWords(newStreet));
                            addressComponents.put("City", unitAndCity[2]);
                        }
                    } else {
                        addressComponents.put("City", capitalizeWords(removeAllCommasAndSpaces(city)));
                    }
                }
            } else {
                // we don't have a Zip
                logger.warn("We have no Zip");
            }
        }

        addressComponents.put("Zip", handleSafely("postalCode",componentLocations, addressBlock));
        addressComponents.put("State", getStateOrProvinceAbbreviation(handleSafely("state",componentLocations, addressBlock)));
        addressComponents.put("Country", handleTypeSafely("state", componentLocations));
        // prevents us from returning any nulls
        validateAddressComponents(addressComponents);
        return addressComponents;
    }

    public static void validateAddressComponents(Map<String, String> addressComponents) {
        // List of required keys
        String[] requiredKeys = {"Zip", "State", "Country", "Street", "City"};
        // Iterate over the required keys and check if they exist in the map
        for (String key : requiredKeys) {
            if (!addressComponents.containsKey(key)) {
                addressComponents.put(key, "???");
                logger.warn("{} not found, creating default with value '???'", key);
            }
        }
    }

    private static String handleSafely(String addressElement, Map<String, MatchedRange> componentLocations, String addressBlock) {
        if(componentLocations.get(addressElement) != null) {
            int start = componentLocations.get(addressElement).getStart();
            int end = componentLocations.get(addressElement).getEnd();
            return addressBlock.substring(start, end);
        }
        return "???";
    }

    private static String handleTypeSafely(String addressElement, Map<String, MatchedRange> componentLocations) {
        if(componentLocations.get(addressElement) != null) {
            return componentLocations.get(addressElement).getType();
        }
        return "???";
    }

    private static MatchedRange findCanadaZipCode(String input) {
        if (input == null || input.isEmpty()) {
            return null; // Return null if the input is invalid
        }
        Matcher matcher = CANADA_POSTAL_PATTERN.matcher(input);
        // Check if the pattern matches in the input string
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            return new MatchedRange(start, end, ""); // Return the range of the ZIP code
        }
        return null; // No match found
    }

    public static String removeAllCommasAndSpaces(String input) {
        if (input == null) {
            return null; // Return null if the input is null
        }
        return input.replace(",", "").trim(); // Remove only commas, preserve spaces
    }

    public static MatchedRange findUSZipCode(String input) {
        if (input == null || input.isEmpty()) {
            return null; // Return null if the input is invalid
        }
        Matcher matcher = US_ZIP_PATTERN.matcher(input);
        // Check if the pattern matches in the input string
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            return new MatchedRange(start, end, ""); // Return the range of the ZIP code
        }
        return null; // No match found
    }

    public static MatchedRange getLastStreetTypeRange(String input) {
        if (input == null || input.isEmpty()) {
            logger.warn("Invalid address input");
            return null; // No match found
        }
        String[] words = input.split("[,\\s]+"); // Split by commas or whitespace
        int currentIndex = 0;
        MatchedRange lastMatchedRange = null;
        for (String word : words) {
            for (String streetType : STREET_TYPES) {
                if (word.equalsIgnoreCase(streetType)) {
                    // Calculate the range of the matched word
                    int end = currentIndex + word.length();
                    lastMatchedRange = new MatchedRange(currentIndex, end, streetType);
                }
            }
            currentIndex += word.length() + 1; // +1 for the space or comma
        }
        if (lastMatchedRange == null) {
            logger.warn("No street type found");
        }
        System.out.println("street type: " + lastMatchedRange);
        return lastMatchedRange;
    }

    public static boolean isFirstWordInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false; // Input is invalid or empty
        }
        // Split the string into words using whitespace as the delimiter
        String[] words = input.trim().split("\\s+");
        // Check if the first word is an integer
        try {
            Integer.parseInt(words[0]);
            return true;
        } catch (NumberFormatException e) {
            return false; // The first word is not a valid integer
        }
    }

    public static MatchedRange parseAddressForLocation(String address) {
        if (address == null || address.isEmpty()) {
            logger.warn("Invalid address input");
            return null; // No match found
        }
        MatchedRange lastMatch = null; // Keep track of the last match
        String[] words = address.split("[,\\s]+"); // Split by commas or whitespace
        int index = 0; // Tracks position in the string
        for (String word : words) {
            int start = address.indexOf(word, index);
            int end = start + word.length();
            // Check for province/state abbreviations (case-insensitive)
            if (StateCodes.PROVINCE_ABBREVIATIONS.values().stream().anyMatch(abbr -> abbr.equalsIgnoreCase(word))) {
                lastMatch = new MatchedRange(start, end, "Canada");
            } else if (StateCodes.STATE_ABBREVIATIONS.values().stream().anyMatch(abbr -> abbr.equalsIgnoreCase(word))) {
                lastMatch = new MatchedRange(start, end, "USA");
            }
            // Update index for the next word
            index = end + 1;
        }
        if (lastMatch == null) {
            // Check for full names (case-insensitive)
            index = 0;
            for (String word : words) {
                int start = address.indexOf(word, index);
                int end = start + word.length();
                if (StateCodes.PROVINCE_ABBREVIATIONS.keySet().stream().anyMatch(name -> name.equalsIgnoreCase(word))) {
                    lastMatch = new MatchedRange(start, end, "Canada");
                } else if (StateCodes.STATE_ABBREVIATIONS.keySet().stream().anyMatch(name -> name.equalsIgnoreCase(word))) {
                    lastMatch = new MatchedRange(start, end, "USA");
                }
                index = end + 1;
            }
        }
        return lastMatch; // Return the last match found, or null if none found
    }

    public static int containsStreetType(String input) {
        if (input == null || input.isEmpty()) {
            logger.warn("Invalid address input");
            return 0; // No match found
        }
        String[] words = input.split("[,\\s]+"); // Split by commas or whitespace
        int numberOfStreetTypes = 0;
        for (String word : words) {
            for (String streetType : STREET_TYPES) {
                if (word.equalsIgnoreCase(streetType)) {
                    numberOfStreetTypes++; // Found a match
                }
            }
        }
        return numberOfStreetTypes; // No match found
    }

    public static int countLines(String input) {
        if (input == null || input.isEmpty()) {
            return 0; // Return 0 if the input is null or empty
        }
        // Split by \n and return the length of the resulting array
        return input.split("\n").length;
    }

    public static int countCommas(String input) {
        // Check if input is null or empty
        if (input == null || input.isEmpty()) {
            return 0;
        }
        // Count commas using a stream
        return (int) input.chars().filter(ch -> ch == ',').count();
    }

    public static boolean containsUnitDesignator(String input) {
        if (input == null || input.isEmpty()) {
            return false; // Return false if the input is null or empty
        }
        // Use a case-insensitive search
        for (String designator : UNIT_DESIGNATORS) {
            if (input.toLowerCase().contains(designator.toLowerCase())) {
                return true; // Return true if any designator is found
            }
        }
        return false; // Return false if no designators are found
    }

    public static String[] extractUnitAndRemainingText(String input) {
        if (input == null || input.isEmpty()) {
            return null; // Return null if the input is null or empty
        }
        String[] words = splitIntoThreeParts(input);
        if (words.length < 2) {
            return null; // Return null if there isn't at least two parts
        }
        // Check if the first word matches a UNIT_DESIGNATOR
        for (String designator : UNIT_DESIGNATORS) {
            if (words[0].equalsIgnoreCase(designator)) {
                return new String[]{words[0], words[1], words[2]}; // Return the designator and the remaining text
            }
        }
        return null; // Return null if no match is found
    }

    public static String[] splitIntoThreeParts(String input) {
        if (input == null || input.isBlank()) {
            return new String[]{"", "", ""}; // Handle empty or null input
        }
        // Split by whitespace into three parts
        String[] words = input.split("\\s+", 3); // At most 3 parts
        String firstWord = words.length > 0 ? words[0] : "";
        String secondWord = words.length > 1 ? words[1] : "";
        String rest = words.length > 2 ? words[2] : "";
        return new String[]{firstWord, secondWord, rest};
    }

    public static String removeDots(String input) {
        if (input == null) {
            return null; // Handle null input gracefully
        }
        return input.replace(".", ""); // Replace all "." with an empty string
    }

    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return the original input if it's null or empty
        }
        // Split the string into words
        String[] words = input.split("\\s+");
        // Use a StringBuilder to construct the output
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                // Capitalize the first letter and make the rest lowercase
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                capitalized.append(capitalizedWord).append(" ");
            }
        }
        // Remove the trailing space and return the result
        return capitalized.toString().trim();
    }

    public static String getStateOrProvinceAbbreviation(String input) {
        if (input == null || input.isEmpty()) {
            return "???"; // Return default for null or empty input
        }
        input = input.trim(); // Remove leading and trailing spaces
        if (input.length() == 2) {
            return input.toUpperCase(); // Ensure two-character input is capitalized
        } else if (input.length() > 2) {
            // Check in STATE_ABBREVIATIONS
            if (StateCodes.STATE_ABBREVIATIONS.containsKey(input)) {
                return StateCodes.STATE_ABBREVIATIONS.get(input);
            }
            // Check in PROVINCE_ABBREVIATIONS
            if (StateCodes.PROVINCE_ABBREVIATIONS.containsKey(input)) {
                return StateCodes.PROVINCE_ABBREVIATIONS.get(input);
            }
        }
        return "???"; // Return default if no match is found
    }

    public static String processAndCombineLines(String input) {
        if (input == null || input.isEmpty()) {
            return ""; // Return empty string if input is null or empty
        }
        // Split the input by "\n"
        String[] lines = input.split("\\n");
        // Combine the remaining elements with a space
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            if (!lines[i].trim().isEmpty()) { // Ignore empty lines
                if (result.length() > 0) {
                    result.append(" "); // Add space between lines
                }
                result.append(lines[i].trim());
            }
        }
        return result.toString();
    }

}



