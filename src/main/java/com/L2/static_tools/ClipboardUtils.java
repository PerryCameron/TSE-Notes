package com.L2.static_tools;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClipboardUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClipboardUtils.class);

    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
        HANDLE GlobalAlloc(int uFlags, int dwBytes);
        Pointer GlobalLock(HANDLE hMem);
        boolean GlobalUnlock(HANDLE hMem);
        HANDLE GlobalFree(HANDLE hMem);
    }

    interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean OpenClipboard(HWND hWnd);
        boolean EmptyClipboard();
        boolean CloseClipboard();
        HANDLE SetClipboardData(int uFormat, HANDLE hMem);
        HWND GetActiveWindow();
        int RegisterClipboardFormat(String format);
    }

    // this is original
//    public static void copyHtmlToClipboard(String html, String plainText) {
//        String htmlHeader = "Version:0.9\r\n" +
//                "StartHTML:%1$010d\r\n" +
//                "EndHTML:%2$010d\r\n" +
//                "StartFragment:%3$010d\r\n" +
//                "EndFragment:%4$010d\r\n";
//        String startFragment = "<!--StartFragment-->";
//        String endFragment = "<!--EndFragment-->";
//        String htmlContent = "<html><body>" + startFragment + html + endFragment + "</body></html>";
//        int startHTML = htmlHeader.length() - 40; // 40 is the length of the placeholders
//        int startFragmentIndex = startHTML + htmlContent.indexOf(startFragment);
//        int endFragmentIndex = startHTML + htmlContent.indexOf(endFragment) + endFragment.length();
//        int endHTML = startHTML + htmlContent.length();
//        String formattedHtml = String.format(htmlHeader, startHTML, endHTML, startFragmentIndex, endFragmentIndex) + htmlContent;
//        // Plain text version (strip out HTML tags)
//        // plainText = html.replaceAll("<[^>]+>", "");
//        // Set the clipboard data
//        User32.INSTANCE.OpenClipboard(null);
//        User32.INSTANCE.EmptyClipboard();
//        try {
//            // Set HTML format
//            int CF_HTML = User32.INSTANCE.RegisterClipboardFormat("HTML Format");
//            setClipboardData(CF_HTML, formattedHtml);
//            // Set Plain Text format
//            int CF_TEXT = 1; // 1 is CF_TEXT, which is the standard text format
//            setClipboardData(CF_TEXT, plainText);
//        } finally {
//            User32.INSTANCE.CloseClipboard();
//        }
//    }

// this is new below 2nd attempt
//    public static void copyHtmlToClipboard(String html, String plainText) {
//        // HTML clipboard format with correct lengths
//        String htmlHeader = "Version:0.9\r\n" +
//                "StartHTML:%1$010d\r\n" +
//                "EndHTML:%2$010d\r\n" +
//                "StartFragment:%3$010d\r\n" +
//                "EndFragment:%4$010d\r\n";
//        String startFragment = "<!--StartFragment-->";
//        String endFragment = "<!--EndFragment-->";
//        // Replace non-breaking spaces and ensure proper encoding
//        html = html.replace("\u00A0", " ").replace("&nbsp;", " ");
//        String htmlContent = "<html><body>" + startFragment + html + endFragment + "</body></html>";
//        int startHTML = htmlHeader.length() - 40; // 40 is the length of the placeholders
//        int startFragmentIndex = startHTML + htmlContent.indexOf(startFragment);
//        int endFragmentIndex = startHTML + htmlContent.indexOf(endFragment) + endFragment.length();
//        int endHTML = startHTML + htmlContent.length();
//        String formattedHtml = String.format(htmlHeader, startHTML, endHTML, startFragmentIndex, endFragmentIndex) + htmlContent;
//        // Set the clipboard data
//        User32.INSTANCE.OpenClipboard(null);
//        User32.INSTANCE.EmptyClipboard();
//        try {
//            // Set HTML format
//            int CF_HTML = User32.INSTANCE.RegisterClipboardFormat("HTML Format");
//            setClipboardData(CF_HTML, formattedHtml);
//            // Set Plain Text format
//            int CF_TEXT = 1; // 1 is CF_TEXT, which is the standard text format
//            setClipboardData(CF_TEXT, plainText);
//        } finally {
//            User32.INSTANCE.CloseClipboard();
//        }
//    }

    // this is the third version that also affects the printing of the table
//    public static void copyHtmlToClipboard(String html, String plainText) {
//        // HTML clipboard format with correct lengths
//        System.out.println(html);
//        System.out.println(plainText);
//        String htmlHeader = "Version:0.9\r\n" +
//                "StartHTML:%1$010d\r\n" +
//                "EndHTML:%2$010d\r\n" +
//                "StartFragment:%3$010d\r\n" +
//                "EndFragment:%4$010d\r\n";
//        String startFragment = "<!--StartFragment-->";
//        String endFragment = "<!--EndFragment-->";
//
//
//        // Replace non-breaking spaces and ensure proper encoding
//        html = html.replace("\u00A0", " ").replace("&nbsp;", " ");
//        String htmlContent = "<html><body>" + startFragment + html + endFragment + "</body></html>";
//        int startHTML = htmlHeader.length(); // Updated for clarity
//        int startFragmentIndex = startHTML + htmlContent.indexOf(startFragment);
//        int endFragmentIndex = startHTML + htmlContent.indexOf(endFragment) + endFragment.length();
//        int endHTML = startHTML + htmlContent.length();
//        String formattedHtml = String.format(htmlHeader, startHTML, endHTML, startFragmentIndex, endFragmentIndex) + htmlContent;
//
//
//        // Set the clipboard data
//        try {
//            if (!User32.INSTANCE.OpenClipboard(null)) {
//                throw new IllegalStateException("Could not open clipboard");
//            }
//            User32.INSTANCE.EmptyClipboard();
//
//
//            // Set HTML format
//            int CF_HTML = User32.INSTANCE.RegisterClipboardFormat("HTML Format");
//            if (CF_HTML == 0) {
//                throw new IllegalStateException("Failed to register HTML Format");
//            }
//            setClipboardData(CF_HTML, formattedHtml);
//
//
//            // Set Plain Text format
//            int CF_TEXT = 1; // 1 is CF_TEXT, the standard text format
//            setClipboardData(CF_TEXT, plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            User32.INSTANCE.CloseClipboard();
//        }
//    }
    // attempt 4
    public static void copyHtmlToClipboard(String html, String plainText) {
        try {
            // Ensure proper encoding for HTML
            byte[] htmlBytes = html.getBytes(StandardCharsets.UTF_8);
            String utf8Html = new String(htmlBytes, StandardCharsets.UTF_8);


            // Log bytes for debugging
//            System.out.println("HTML Bytes: " + Arrays.toString(htmlBytes));

            // Ensure proper encoding for plain text
            byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
            String utf8PlainText = new String(plainTextBytes, StandardCharsets.UTF_8);


            // HTML clipboard format with correct lengths
            String htmlHeader = "Version:0.9\r\n" +
                    "StartHTML:%1$010d\r\n" +
                    "EndHTML:%2$010d\r\n" +
                    "StartFragment:%3$010d\r\n" +
                    "EndFragment:%4$010d\r\n";
            String startFragment = "<!--StartFragment-->";
            String endFragment = "<!--EndFragment-->";
            String htmlContent = "<html><body>" + startFragment + utf8Html + endFragment + "</body></html>";


            int startHTML = htmlHeader.length() - 40; // 40 is the length of the placeholders
            int startFragmentIndex = startHTML + htmlContent.indexOf(startFragment);
            int endFragmentIndex = startHTML + htmlContent.indexOf(endFragment) + endFragment.length();
            int endHTML = startHTML + htmlContent.length();


            String formattedHtml = String.format(htmlHeader, startHTML, endHTML, startFragmentIndex, endFragmentIndex) + htmlContent;


            // Set the clipboard data
            User32.INSTANCE.OpenClipboard(null);
            User32.INSTANCE.EmptyClipboard();
            try {
                // Set HTML format
                int CF_HTML = User32.INSTANCE.RegisterClipboardFormat("HTML Format");
                setClipboardData(CF_HTML, formattedHtml);


                // Set Plain Text format
                int CF_TEXT = 1; // 1 is CF_TEXT, which is the standard text format
                setClipboardData(CF_TEXT, utf8PlainText);
            } finally {
                User32.INSTANCE.CloseClipboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String escapeHtmlContent(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;") // Escape ampersand first
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("á", "&aacute;")
                .replace("é", "&eacute;")
                .replace("í", "&iacute;")
                .replace("ó", "&oacute;")
                .replace("ú", "&uacute;")
                .replace("Á", "&Aacute;")
                .replace("É", "&Eacute;")
                .replace("Í", "&Iacute;")
                .replace("Ó", "&Oacute;")
                .replace("Ú", "&Uacute;")
                .replace("ñ", "&ntilde;")
                .replace("Ñ", "&Ntilde;")
                .replace("¡", "&iexcl;")
                .replace("¿", "&iquest;");
    }














    private static void setClipboardData(int format, String data) {
        int dataSize = data.length() + 1; // +1 for null terminator
        HANDLE hGlobal = Kernel32.INSTANCE.GlobalAlloc(0x2000, dataSize); // GMEM_MOVEABLE = 0x2000
        if (hGlobal == null) {
            throw new RuntimeException("GlobalAlloc failed");
        }
        try {
            Pointer pGlobal = Kernel32.INSTANCE.GlobalLock(hGlobal);
            if (pGlobal == null) {
                throw new RuntimeException("GlobalLock failed");
            }
            try {
                pGlobal.setString(0, data);
            } finally {
                Kernel32.INSTANCE.GlobalUnlock(hGlobal);
            }
            HANDLE result = User32.INSTANCE.SetClipboardData(format, hGlobal);
            if (result == null) {
                throw new RuntimeException("SetClipboardData failed");
            }
            // DO NOT call GlobalFree(hGlobal) here. The clipboard now owns the memory.
        } catch (RuntimeException e) {
            // If SetClipboardData failed, clean up memory manually
            Kernel32.INSTANCE.GlobalFree(hGlobal);
            throw e;
        }
    }

    // Method to retrieve text from clipboard
    public static String getClipboardText() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable data = clipboard.getContents(null);
            // Check if the clipboard contains text data
            if (data != null && data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    return (String) data.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("Clipboard does not contain string data.");
            }
        } catch (Exception e) {
            // Catch any exception that isn't related to text data (optional)
            logger.error("An error occurred while retrieving clipboard data.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}







