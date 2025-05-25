package com.L2.static_tools;

import com.L2.dto.ProductFamilyFx;

import java.util.ArrayList;
import java.util.List;

public class JsonDeserialize {

    public static List<ProductFamilyFx> deserializeArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string is null or empty");
        }

        // Remove whitespace and check for array
        String cleanedJson = json.trim().replaceAll("\\s+", "");
        if (!cleanedJson.startsWith("[") || !cleanedJson.endsWith("]")) {
            throw new IllegalArgumentException("Invalid JSON: must be an array");
        }

        // Handle empty array
        if (cleanedJson.equals("[]")) {
            return new ArrayList<>();
        }

        // Remove outer brackets
        cleanedJson = cleanedJson.substring(1, cleanedJson.length() - 1);
        List<ProductFamilyFx> result = new ArrayList<>();

        // Split into individual objects
        String[] objectStrs = splitJsonObjects(cleanedJson);
        for (String objectStr : objectStrs) {
            ProductFamilyFx productFamilyFx = deserializeObject(objectStr);
            result.add(productFamilyFx);
        }

        return result;
    }

    private static ProductFamilyFx deserializeObject(String json) {
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON object: " + json);
        }

        // Remove outer braces
        json = json.substring(1, json.length() - 1);

        String range = null;
        List<String> productFamilies = new ArrayList<>();

        // Split by commas, respecting array boundaries
        String[] parts = splitJsonFields(json);
        for (String part : parts) {
            if (part.startsWith("\"range\":")) {
                range = extractStringValue(part.substring("\"range\":".length()));
            } else if (part.startsWith("\"product_families\":")) {
                String arrayStr = part.substring("\"product_families\":".length());
                productFamilies = extractArrayValues(arrayStr);
            }
        }

        if (range == null) {
            throw new IllegalArgumentException("JSON missing 'range' field in object: " + json);
        }

        return new ProductFamilyFx(range, productFamilies);
    }

    private static String[] splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int start = 0;
        int braceCount = 0;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
            } else if (c == ',' && braceCount == 0) {
                objects.add(json.substring(start, i));
                start = i + 1;
            }
        }
        if (start < json.length()) {
            objects.add(json.substring(start));
        }
        return objects.toArray(new String[0]);
    }

    private static String[] splitJsonFields(String json) {
        List<String> fields = new ArrayList<>();
        int start = 0;
        int braceCount = 0;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '[' || c == '{') {
                braceCount++;
            } else if (c == ']' || c == '}') {
                braceCount--;
            } else if (c == ',' && braceCount == 0) {
                fields.add(json.substring(start, i));
                start = i + 1;
            }
        }
        if (start < json.length()) {
            fields.add(json.substring(start));
        }
        return fields.toArray(new String[0]);
    }

    private static String extractStringValue(String value) {
        if (!value.startsWith("\"") || !value.endsWith("\"")) {
            throw new IllegalArgumentException("Invalid string value: " + value);
        }
        return value.substring(1, value.length() - 1).replace("\\\"", "\"");
    }

    private static List<String> extractArrayValues(String arrayStr) {
        if (!arrayStr.startsWith("[") || !arrayStr.endsWith("]")) {
            throw new IllegalArgumentException("Invalid array: " + arrayStr);
        }
        List<String> values = new ArrayList<>();
        if (arrayStr.equals("[]")) {
            return values;
        }

        String content = arrayStr.substring(1, arrayStr.length() - 1);
        String[] elements = splitJsonFields(content);
        for (String element : elements) {
            values.add(extractStringValue(element));
        }
        return values;
    }
}
