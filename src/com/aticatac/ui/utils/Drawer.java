package com.aticatac.ui.utils;

import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Drawer {

    public static void title(GraphicsContext gc, long time) {
        gc.save();

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        double animation = (double) time / 1000000000;

        gc.rotate(Math.sin(animation));

        Color col = Color.color(abs(sin(animation)), abs(cos(0.5 * animation)), abs(cos(0.3 * animation)));

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(col);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(Font.font("Carlito", FontWeight.BOLD, 48));
        gc.translate(2 * sin(animation),sin(animation));
        gc.fillText("ATACATIC", width / 2, height / 7);
        gc.strokeText("ATACATIC", width / 2, height / 7);

        gc.restore();
    }

    public static void menuItems(GraphicsContext gc, String text) {



    }

    public static void background(GraphicsContext gc, Color color) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        gc.setFill(color);
        gc.fillRect(0, 0, width, height);

    }
}
