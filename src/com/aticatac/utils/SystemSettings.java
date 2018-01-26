package com.aticatac.utils;

public class SystemSettings {
    private static int screenWidth;
    private static int screenHeight;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        SystemSettings.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        SystemSettings.screenHeight = screenHeight;
    }
}
