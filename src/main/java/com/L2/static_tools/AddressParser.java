package com.L2.static_tools;
import java.util.*;
import java.util.regex.*;


public class AddressParser {
    // List of common street types
    private static final List<String> STREET_TYPES = Arrays.asList(
            "St","St.", "Street", "Ave", "Avenue", "Rd","Rd.", "Road", "Blvd", "Boulevard", "Ln","Ln.", "Lane", "Dr", "Dr.", "Drive", "Ct", "Ct.", "Court", "Pl", "Pl.", "Place", "Terrace", "Way", "Circle", "Cir", "Cir."
    );
    private static final List<String> UNIT_DESIGNATORS = Arrays.asList(
            "Apt", "Apt.", "Suite", "Ste", "Unit", "Fl", "Floor", "Rm", "Room", "Bldg", "Building"
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
        return parseAddress(addressBlock);
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

    private static Map<String, String> parseAddress(String addressBlock) {
        Map<String, String> addressComponents = new HashMap<>();
        Map<String, MatchedRange> componentLocations = new HashMap<>();

        int numberOfCommas = countCommas(addressBlock);
        int numberOfLines = countLines(addressBlock);
        int numberOfStreetTypes = containsStreetType(addressBlock);
        boolean firstWordIsInteger = isFirstWordInteger(addressBlock);
        boolean containsUnit = containsUnitDesignator(addressBlock);

        // does this start with street address
        if(firstWordIsInteger) {
            // we have Rd, Lane, Circle etc. in string
            if(numberOfStreetTypes > 0) {
                MatchedRange streetType = getLastStreetTypeRange(addressBlock);
                if(streetType != null) {
                    componentLocations.put("streetType", streetType);
                    addressComponents.put("Street", addressBlock.substring(0, streetType.getEnd()));
                } else {
                    System.out.println("Street type is null");
                }
            } else {
                if(numberOfCommas > 1) { // possibly a french address seperated by commas?
                    String[] getCommaSeperatedAddressComponents = addressBlock.split(",");
                    if (getCommaSeperatedAddressComponents[0] != null)
                        addressComponents.put("Street", getCommaSeperatedAddressComponents[0].trim());
                    if (getCommaSeperatedAddressComponents[1] != null)
                        addressComponents.put("city", getCommaSeperatedAddressComponents[1].trim());
                }
                System.out.println("Number of StreetTypes is 0");
            }
        } else {
            System.out.println("First word is not an integer");
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
                            addressComponents.put("Street", newStreet);
                            addressComponents.put("City", unitAndCity[2]);
                        }
                    } else {
                        addressComponents.put("City", removeAllCommasAndSpaces(city));
                    }
                }
            } else {
                // we don't have a Zip
                System.out.println("We have no Zip");
            }
        }

        addressComponents.put("Zip", addressBlock.substring(componentLocations.get("postalCode").getStart(), componentLocations.get("postalCode").getEnd()));
        addressComponents.put("State", addressBlock.substring(componentLocations.get("state").getStart(), componentLocations.get("state").getEnd()));
        addressComponents.put("Country", componentLocations.get("state").getType());
//        cleanComponents(addressComponents);
        return addressComponents;
    }

//    private static void cleanComponents(Map<String, String> addressComponents) {
//        addressComponents.put("StreetType", cleanStateOrProvence(addressComponents.get("StreetType")));
//    }
//
//    private static String cleanStateOrProvence(String streetType) {
//        if(streetType.length() == 2) {
//
//        }
//    }


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
        return input.replace(",", "").replace(" ", "");
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
            System.out.println("Invalid address input");
            return null; // No match found
        }
        String[] words = input.split("[,\\s]+"); // Split by commas or whitespace
        int currentIndex = 0;
        MatchedRange lastMatchedRange = null;
        for (String word : words) {
            for (String streetType : STREET_TYPES) {
                if (word.equalsIgnoreCase(streetType)) {
                    // Calculate the range of the matched word
                    int start = currentIndex;
                    int end = start + word.length();
                    lastMatchedRange = new MatchedRange(start, end, streetType);
                }
            }
            currentIndex += word.length() + 1; // +1 for the space or comma
        }
        if (lastMatchedRange == null) {
            System.out.println("No street type found");
        }
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
            System.out.println("Invalid address input");
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
            System.out.println("Invalid address input");
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
    // Helper to check if a part is a street type
    private static boolean isStreetType(String part) {
        for (String streetType : STREET_TYPES) {
            if (part.equalsIgnoreCase(streetType) || part.endsWith(".")) {
                return true;
            }
        }
        return false;
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
}



