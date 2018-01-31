package com.aticatac.ui.utils;

import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UIDrawer {
    public static void background(GraphicsContext gc, Color color) {
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        gc.setFill(color);
        gc.fillRect(0, 0, width, height);
    }
}
