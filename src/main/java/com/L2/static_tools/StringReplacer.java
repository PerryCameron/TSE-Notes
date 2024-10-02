package com.L2.static_tools;

public class StringReplacer {

    /**
     * Replaces all occurrences of "\r\n", "\n", and "\r" with "<br>" in the provided string.
     * This handles Windows (CRLF), Unix/Linux (LF), and classic Mac (CR) line endings.
     * @param input The string to be processed.
     * @return A new string with all types of line breaks replaced by "<br>".
     */
    public static String replaceAnyNewLineWithBr(String input) {
        if (input == null) {
            return null;  // Return null if the input string is null
        }
        return input.replaceAll("\\r\\n|\\n|\\r", "<br>");
    }

    public static void main(String[] args) {
        String testString = """
                Power module fault and UPS is beeping.  Module A3 main frame.
                
                I am testing this note
                
                Because I can...
                """;


        // Test with various types of newlines
        String windowsStyle = "Line 1\r\nLine 2\r\nLine 3";   // CRLF (Windows)
        String unixStyle = "Line 1\nLine 2\nLine 3";          // LF (Unix/Linux)
        String macStyle = "Line 1\rLine 2\rLine 3";           // CR (Classic Mac)

        System.out.println("Original Windows-style String:\n" + testString);
        System.out.println("Processed String:\n" + replaceAnyNewLineWithBr(testString));

        System.out.println("\nOriginal Unix-style String:\n" + unixStyle);
        System.out.println("Processed String:\n" + replaceAnyNewLineWithBr(unixStyle));

        System.out.println("\nOriginal Mac-style String:\n" + macStyle);
        System.out.println("Processed String:\n" + replaceAnyNewLineWithBr(macStyle));
    }
}

