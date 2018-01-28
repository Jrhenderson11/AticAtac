package com.aticatac.ui.utils;

import com.aticatac.utils.SystemSettings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Placeholder extends Scene {
    public Placeholder(Group root) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Drawer.background(gc, Color.YELLOW);
    }
}
