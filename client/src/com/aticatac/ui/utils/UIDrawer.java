package com.aticatac.ui.utils;

import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIDrawer {
    public static final Font TITLE_FONT = Font.font("Nimbus Mono L", FontWeight.BOLD, 48);
    public static final Font OPTION_TEXT = Font.font("Courier 10 Pitch", FontWeight.BOLD, 25);
    public static final Font LOBBYBROWSTEXT = Font.font("Gentium", FontWeight.BOLD, 18);

    public static void background(GraphicsContext gc, Color color) {
        gc.save();
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        gc.setFill(color);
        gc.fillRect(0, 0, width, height);
        gc.restore();
    }
}
