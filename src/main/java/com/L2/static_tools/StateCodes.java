package com.L2.static_tools;

import java.util.HashMap;
import java.util.Map;

public class StateCodes {
    public static final Map<String, String> STATE_ABBREVIATIONS = new HashMap<>();
    public static final Map<String, String> PROVINCE_ABBREVIATIONS = new HashMap<>();

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey(); // Return the key when a matching value is found
            }
        }
        return null; // Return null if the value is not found
    }

    static {
        // USA State abbreviations
        STATE_ABBREVIATIONS.put("Alabama", "AL");
        STATE_ABBREVIATIONS.put("Alaska", "AK");
        STATE_ABBREVIATIONS.put("Arizona", "AZ");
        STATE_ABBREVIATIONS.put("Arkansas", "AR");
        STATE_ABBREVIATIONS.put("California", "CA");
        STATE_ABBREVIATIONS.put("Colorado", "CO");
        STATE_ABBREVIATIONS.put("Connecticut", "CT");
        STATE_ABBREVIATIONS.put("Delaware", "DE");
        STATE_ABBREVIATIONS.put("Florida", "FL");
        STATE_ABBREVIATIONS.put("Georgia", "GA");
        STATE_ABBREVIATIONS.put("Hawaii", "HI");
        STATE_ABBREVIATIONS.put("Idaho", "ID");
        STATE_ABBREVIATIONS.put("Illinois", "IL");
        STATE_ABBREVIATIONS.put("Indiana", "IN");
        STATE_ABBREVIATIONS.put("Iowa", "IA");
        STATE_ABBREVIATIONS.put("Kansas", "KS");
        STATE_ABBREVIATIONS.put("Kentucky", "KY");
        STATE_ABBREVIATIONS.put("Louisiana", "LA");
        STATE_ABBREVIATIONS.put("Maine", "ME");
        STATE_ABBREVIATIONS.put("Maryland", "MD");
        STATE_ABBREVIATIONS.put("Massachusetts", "MA");
        STATE_ABBREVIATIONS.put("Michigan", "MI");
        STATE_ABBREVIATIONS.put("Minnesota", "MN");
        STATE_ABBREVIATIONS.put("Mississippi", "MS");
        STATE_ABBREVIATIONS.put("Missouri", "MO");
        STATE_ABBREVIATIONS.put("Montana", "MT");
        STATE_ABBREVIATIONS.put("Nebraska", "NE");
        STATE_ABBREVIATIONS.put("Nevada", "NV");
        STATE_ABBREVIATIONS.put("New Hampshire", "NH");
        STATE_ABBREVIATIONS.put("New Jersey", "NJ");
        STATE_ABBREVIATIONS.put("New Mexico", "NM");
        STATE_ABBREVIATIONS.put("New York", "NY");
        STATE_ABBREVIATIONS.put("North Carolina", "NC");
        STATE_ABBREVIATIONS.put("North Dakota", "ND");
        STATE_ABBREVIATIONS.put("Ohio", "OH");
        STATE_ABBREVIATIONS.put("Oklahoma", "OK");
        STATE_ABBREVIATIONS.put("Oregon", "OR");
        STATE_ABBREVIATIONS.put("Pennsylvania", "PA");
        STATE_ABBREVIATIONS.put("Rhode Island", "RI");
        STATE_ABBREVIATIONS.put("South Carolina", "SC");
        STATE_ABBREVIATIONS.put("South Dakota", "SD");
        STATE_ABBREVIATIONS.put("Tennessee", "TN");
        STATE_ABBREVIATIONS.put("Texas", "TX");
        STATE_ABBREVIATIONS.put("Utah", "UT");
        STATE_ABBREVIATIONS.put("Vermont", "VT");
        STATE_ABBREVIATIONS.put("Virginia", "VA");
        STATE_ABBREVIATIONS.put("Washington", "WA");
        STATE_ABBREVIATIONS.put("West Virginia", "WV");
        STATE_ABBREVIATIONS.put("Wisconsin", "WI");
        STATE_ABBREVIATIONS.put("Wyoming", "WY");

        // Canada Province abbreviations
        PROVINCE_ABBREVIATIONS.put("Alberta", "AB");
        PROVINCE_ABBREVIATIONS.put("British Columbia", "BC");
        PROVINCE_ABBREVIATIONS.put("Manitoba", "MB");
        PROVINCE_ABBREVIATIONS.put("New Brunswick", "NB");
        PROVINCE_ABBREVIATIONS.put("Newfoundland and Labrador", "NL");
        PROVINCE_ABBREVIATIONS.put("Nova Scotia", "NS");
        PROVINCE_ABBREVIATIONS.put("Ontario", "ON");
        PROVINCE_ABBREVIATIONS.put("Prince Edward Island", "PE");
        PROVINCE_ABBREVIATIONS.put("Quebec", "QC");
        PROVINCE_ABBREVIATIONS.put("Saskatchewan", "SK");
        PROVINCE_ABBREVIATIONS.put("Northwest Territories", "NT");
        PROVINCE_ABBREVIATIONS.put("Nunavut", "NU");
        PROVINCE_ABBREVIATIONS.put("Yukon", "YT");
    }

}
