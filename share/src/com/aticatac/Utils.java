package com.aticatac;

import javafx.scene.paint.Color;

public class Utils {
    public static Color intToColor(int colorInt) {

        switch (colorInt) {
            case 0:
                return Color.YELLOW;
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            default:
                return Color.CYAN;
        }

    }
}
