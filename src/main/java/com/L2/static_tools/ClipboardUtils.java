package com.L2.static_tools;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinDef.HWND;



public class ClipboardUtils {
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
        int RegisterClipboardFormat(String format);  // RegisterClipboardFormat method
    }


    public static void copyRtfToClipboard(String rtf) {
        if (!User32.INSTANCE.OpenClipboard(null)) {
            System.err.println("Failed to open clipboard");
            return;
        }

        if (!User32.INSTANCE.EmptyClipboard()) {
            System.err.println("Failed to empty clipboard");
            User32.INSTANCE.CloseClipboard();
            return;
        }

        int CF_RTF = User32.INSTANCE.RegisterClipboardFormat("Rich Text Format");
        int dataSize = rtf.length() + 1; // +1 for null terminator
        HANDLE hGlobal = Kernel32.INSTANCE.GlobalAlloc(GMEM_MOVEABLE, dataSize);
        if (hGlobal == null) {
            System.err.println("Failed to allocate global memory");
            User32.INSTANCE.CloseClipboard();
            return;
        }

        Pointer pGlobal = Kernel32.INSTANCE.GlobalLock(hGlobal);
        if (pGlobal == null) {
            System.err.println("Failed to lock global memory");
            User32.INSTANCE.CloseClipboard();
            return;
        }

        pGlobal.setString(0, rtf);

        Kernel32.INSTANCE.GlobalUnlock(hGlobal);
        HANDLE result = User32.INSTANCE.SetClipboardData(CF_RTF, hGlobal);
        if (result == null) {
            System.err.println("Failed to set clipboard data");
        } else {
            System.out.println("Clipboard data set successfully");
        }

        User32.INSTANCE.CloseClipboard();
    }



    public static void main(String[] args) {
        // RTF string that makes "bold" appear bold
        String rtf = "{\\rtf1\\ansi\\ansicpg1252\\uc1 \\b bold\\b0 }";
        copyRtfToClipboard("Will this work " + rtf + " now?");
        System.out.println("RTF formatted text copied to clipboard.");
    }

//<strong lwc-4nfn2rc40ch="">Parts Needed:</strong>
}


//> Task :com.L2.static_tools.ClipboardUtils.main()
//RTF formatted text copied to clipboard.
//
//BUILD SUCCESSFUL in 1s
//3 actionable tasks: 2 executed, 1 up-to-date
//12:10:25 PM: Execution finished ':com.L2.static_tools.ClipboardUtils.main()'.

// Going to teams and ctl-C left nothing










