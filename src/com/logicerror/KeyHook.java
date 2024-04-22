package com.logicerror;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

/** Sample implementation of a low-level keyboard hook on W32. */
public class KeyHook extends Thread {
    private static volatile boolean quit;
    private static volatile boolean keybindDown;
    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    final static User32 lib = User32.INSTANCE;

    public static void main(String[] args) {
        KeyHook hook = new KeyHook();
        hook.start();
        System.out.println("listening");
//        System.out.println(hook.isKeybindPressed());
        while (!hook.isKeybindDown()){

        }
        hook.quit();
//        System.out.println("key pressed");
    }
    public boolean isKeybindDown(){
        return keybindDown;
    }

    public void quit(){
        quit = true;
    }

    @Override
    public void run(){
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        setKeyboardHook();

        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
        System.out.println("Keyboard hook installed, type anywhere, 'q' to quit");

        startThread();

        // This bit never returns from GetMessage
        handleMessage();

        lib.UnhookWindowsHookEx(hhk);
    }


    private static void setKeyboardHook(){
        int keyCode = 8;
        keyboardHook = new LowLevelKeyboardProc() {
            @Override
            public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
                if (nCode >= 0) {
                    switch(wParam.intValue()) {
                        case WinUser.WM_KEYUP:
                            System.err.println("in UP, key=" + info.vkCode);
                            if (info.vkCode == keyCode) {
                                keybindDown = false;
                            }
                            break;
                        case WinUser.WM_KEYDOWN:
                            System.err.println("in DOWN, key=" + info.vkCode);
                            if (info.vkCode == keyCode) {
                                keybindDown = true;
                            }
                            break;
                        case WinUser.WM_SYSKEYUP:
                            break;
                        case WinUser.WM_SYSKEYDOWN:
                            break;
                    }
                }

                Pointer ptr = info.getPointer();
                long peer = Pointer.nativeValue(ptr);
                return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
            }
        };
    }

    private static void startThread(){
        new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    try { Thread.sleep(10); } catch(Exception e) { }
                }
                System.err.println("unhook and exit");
                lib.UnhookWindowsHookEx(hhk);
                System.exit(0);
            }
        }.start();
    }

    private static void handleMessage(){
        int result;
        MSG msg = new MSG();
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            }
            else {
                System.err.println("got message");
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
    }
}
