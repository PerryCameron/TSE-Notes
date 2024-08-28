package com.L2.static_tools;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.HWND;


public class ClipboardUtils {

// For some reason when this class is called once it works fine and I can paste HTML to salesforce, Word, and Teams, and Notepad++ gets plain text
    // however , when I hit the copy button a second time and this method is called again it crashes the application after a few seconds
//    private static final int GMEM_MOVEABLE = 0x0002;

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

    public static void copyHtmlToClipboard(String html, String plainText) {
        // HTML clipboard format with correct lengths
        String htmlHeader = "Version:0.9\r\n" +
                "StartHTML:%1$010d\r\n" +
                "EndHTML:%2$010d\r\n" +
                "StartFragment:%3$010d\r\n" +
                "EndFragment:%4$010d\r\n";
        String startFragment = "<!--StartFragment-->";
        String endFragment = "<!--EndFragment-->";
        String htmlContent = "<html><body>" + startFragment + html + endFragment + "</body></html>";
        int startHTML = htmlHeader.length() - 40; // 40 is the length of the placeholders
        int startFragmentIndex = startHTML + htmlContent.indexOf(startFragment);
        int endFragmentIndex = startHTML + htmlContent.indexOf(endFragment) + endFragment.length();
        int endHTML = startHTML + htmlContent.length();
        String formattedHtml = String.format(htmlHeader, startHTML, endHTML, startFragmentIndex, endFragmentIndex) + htmlContent;
        // Plain text version (strip out HTML tags)
        // plainText = html.replaceAll("<[^>]+>", "");
        // Set the clipboard data
        User32.INSTANCE.OpenClipboard(null);
        User32.INSTANCE.EmptyClipboard();
        try {
            // Set HTML format
            int CF_HTML = User32.INSTANCE.RegisterClipboardFormat("HTML Format");
            setClipboardData(CF_HTML, formattedHtml);
            // Set Plain Text format
            int CF_TEXT = 1; // 1 is CF_TEXT, which is the standard text format
            setClipboardData(CF_TEXT, plainText);
        } finally {
            User32.INSTANCE.CloseClipboard();
        }
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
}







