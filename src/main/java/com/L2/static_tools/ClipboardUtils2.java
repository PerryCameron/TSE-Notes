package com.L2.static_tools;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.HWND;


public class ClipboardUtils2 {


    private static final int GMEM_MOVEABLE = 0x0002;


    interface Kernel32 extends Library {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);


        HANDLE GlobalAlloc(int uFlags, int dwBytes);

        Pointer GlobalLock(HANDLE hMem);

        boolean GlobalUnlock(HANDLE hMem);
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


    public static void copyHtmlToClipboard(String html) {
        // HTML format structure with start and end of HTML fragment
//        String html = "<b>bold</b>";
        String htmlClipboardFormat =
                "Version:0.9\r\n" +
                        "StartHTML:00000097\r\n" +
                        "EndHTML:00000157\r\n" +
                        "StartFragment:00000129\r\n" +
                        "EndFragment:00000143\r\n" +
                        "<html><body><!--StartFragment-->" + html + "<!--EndFragment--></body></html>";


        User32.INSTANCE.OpenClipboard(null);
        User32.INSTANCE.EmptyClipboard();


        int CF_HTML = User32.INSTANCE.RegisterClipboardFormat("HTML Format");
        int dataSize = htmlClipboardFormat.length() + 1; // +1 for null terminator
        HANDLE hGlobal = Kernel32.INSTANCE.GlobalAlloc(GMEM_MOVEABLE, dataSize);
        Pointer pGlobal = Kernel32.INSTANCE.GlobalLock(hGlobal);


        pGlobal.setString(0, htmlClipboardFormat);


        Kernel32.INSTANCE.GlobalUnlock(hGlobal);
        HANDLE result = User32.INSTANCE.SetClipboardData(CF_HTML, hGlobal);
        if (result == null) {
            System.err.println("Failed to set clipboard data");
        } else {
            System.out.println("HTML clipboard data set successfully");
        }


        User32.INSTANCE.CloseClipboard();
    }


    public static void main(String[] args) {
        copyHtmlToClipboard("""
                <table border="1">
                    <tr>
                        <th>Part Number</th>
                        <th>Description</th>
                        <th>Qty</th>
                    </tr>
                    <tr>
                        <td>PN-001</td>
                        <td>Widget A</td>
                        <td>10</td>
                    </tr>
                    <tr>
                        <td>PN-002</td>
                        <td>Widget B</td>
                        <td>20</td>
                    </tr>
                </table>
                """);
        System.out.println("HTML formatted text copied to clipboard.");
    }
}







