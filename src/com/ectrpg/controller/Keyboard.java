package com.ectrpg.controller;

import java.awt.event.KeyEvent;

public final class Keyboard {
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;
    public static final int KEY_LEFT = 2;
    public static final int KEY_RIGHT = 3;
    public static final int KEY_FAST = 4;
    public static final int KEY_MENU = 5;
    public static final int KEY_PAUSE = 6;
    public static final int KEY_USE = 7;
    public static final int KEY_BACK = 8;
    public static final boolean STATE_MOVING = false;
    public static final boolean STATE_DIALOG = true;
    private static boolean state = STATE_MOVING;
    private static boolean change = true;
    private static boolean[] keyLock = {false, false, false, false, false, false, false, false, false};
    private static boolean[] keyState = {false, false, false, false, false, false, false, false, false};
    private static boolean[] keyBuffer = {false, false, false, false, false, false, false, false, false};
    private static int[] keySequenc = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT,
            KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE, KeyEvent.VK_X, KeyEvent.VK_Z};
    private boolean state1;

    private Keyboard() {
    }

    public static boolean isKeyPressed(int key, boolean state) {
        return (Keyboard.state == state) && ((key <= 8) && (key >= 0) && !keyLock[key] && keyState[key]);
    }

    public static void setState(boolean state) {
        Keyboard.state = state;
    }

    public static boolean isKeyPressedWithoutLock(int key, boolean state) {
        return (Keyboard.state == state) && ((key <= 8) && (key >= 0) && keyState[key]);
    }

    public static void setPressed(int keyCode, boolean pressed) {
        if (change) {
            for (int key = 0; key < keySequenc.length; key++) {
                if (keySequenc[key] == keyCode) {
                    keyState[key] = pressed;
                }
            }
        }
        for (int key = 0; key < keySequenc.length; key++) {
            if (keySequenc[key] == keyCode) {
                keyBuffer[key] = pressed;
            }
        }
    }

    public static void setChange(boolean change) {
        Keyboard.change = change;
        System.arraycopy(keyBuffer, 0, keyState, 0, keyState.length);
    }

    public static void lock(int key) {
        if ((key <= 3) && (key >= 0)) {
            keyLock[key] = true;
        }
    }

    public static void releaseLock() {
        for (int i = 0; i <= 3; i++) {
            keyLock[i] = false;
        }
    }

    public static String stateToString() {
        StringBuilder key = new StringBuilder();
        for (boolean pressed : keyState) {
            if (pressed) {
                key.append("1");
            } else {
                key.append("0");
            }
        }
        return key.toString();
    }

    public static void show() {
        for (int i = 0; i < 9; i++) {
            System.out.print(keyState[i]);
            System.out.print(" ");
        }
        System.out.println();
        for (int i = 0; i < 9; i++) {
            System.out.print(keyLock[i]);
            System.out.print(" ");
        }
        System.out.println();
        for (int i = 0; i < 9; i++) {
            System.out.print(keyBuffer[i]);
            System.out.print(" ");
        }
        System.out.println();
    }
}
