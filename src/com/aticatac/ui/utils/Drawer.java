package com.aticatac.ui.utils;

import com.aticatac.ui.mainmenu.MenuItem;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

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

    public static void menuItems(GraphicsContext gc, ArrayList<MenuItem> items, long time) {

        double animation = (double) time / 500000000;
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();


        gc.save();

        Color stroke = Color.BLACK;
        Color fill = Color.LIGHTSLATEGREY;
        Color sfill = Color.color(0.5 * abs(sin(animation))
                , 0.5 * abs(cos(0.3 * animation))
                , 0.5 * abs(cos(0.7 * animation)));

        gc.setStroke(stroke);
        gc.setLineWidth(2);

        for(int i = 0; i < items.size(); i++) {
            gc.save();

            MenuItem item = items.get(i);

            if (item.selected()) {
                gc.setFill(sfill);
                gc.translate(0 , 1.5 * sin(animation));
            } else {
                gc.setFill(fill);
            }

            gc.setFont(Font.font("Carlito", FontWeight.BOLD, 30));
            gc.setTextAlign(TextAlignment.CENTER);
            double x = width / 2;
            double y = (i + 2) * height / 9;
            gc.strokeText(item.getName(), x, y);
            gc.fillText(item.getName(), x, y);

            item.setHitbox(x - 100, y - 30,  200, 40);

            gc.restore();

        }

        gc.restore();

    }

    public static void background(GraphicsContext gc, Color color) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        gc.setFill(color);
        gc.fillRect(0, 0, width, height);

    }
}
