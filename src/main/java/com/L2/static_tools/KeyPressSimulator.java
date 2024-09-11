package com.L2.static_tools;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32VK;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;


public class KeyPressSimulator {

    // Define the input type for keyboard
    private static final int INPUT_KEYBOARD = 1;

    public static class KEYBDINPUT extends Structure {
        public WinDef.WORD wVk;           // Virtual key code
        public WinDef.WORD wScan;         // Hardware scan code
        public WinDef.DWORD dwFlags;      // Event type flags
        public WinDef.DWORD time;         // Timestamp for the event
        public PointerByReference dwExtraInfo;  // Extra information

        // Define field order
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("wVk", "wScan", "dwFlags", "time", "dwExtraInfo");
        }

        // Constants for key event flags
        public static final int KEYEVENTF_EXTENDEDKEY = 0x0001;  // Extended key flag
        public static final int KEYEVENTF_KEYUP = 0x0002;        // Key up flag
    }

    public static void simulateEnterKey() {
        // Create an INPUT structure for key down event
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(INPUT_KEYBOARD);  // Use 1 for keyboard input

        // Set the key down event in the KEYBDINPUT structure
        WinUser.KEYBDINPUT kbInput = new WinUser.KEYBDINPUT();
        kbInput.wVk = new WinDef.WORD(Win32VK.VK_RETURN.code);  // Enter key
        kbInput.wScan = new WinDef.WORD(0);  // Scan code (optional)
        kbInput.dwFlags = new WinDef.DWORD(0);  // 0 for key down

        input.input.setType("ki");
        input.input.ki = kbInput;

        // Simulate key down (Enter pressed)
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), new WinUser.INPUT[]{input}, input.size());

        // Set the key up event in the KEYBDINPUT structure
        kbInput.dwFlags = new WinDef.DWORD(KEYBDINPUT.KEYEVENTF_KEYUP);  // Key up flag
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), new WinUser.INPUT[]{input}, input.size());
    }

}

