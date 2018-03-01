package com.aticatac.ui.mainmenu.utils;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.utils.UIDrawer;
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

public class MainMenuDrawer {

    public static void title(GraphicsContext gc, long time) {
        gc.save();

        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        double animation = (double) time / 1000000000;

        gc.rotate(Math.sin(animation));

        Color col = Color.color(abs(sin(animation)), abs(cos(0.5 * animation)), abs(cos(0.3 * animation)));

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(col);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(UIDrawer.TITLE_FONT);
        gc.translate(2 * sin(animation),sin(animation));
        gc.fillText("ATACATIC", width / 2, height / 7);
        gc.strokeText("ATACATIC", width / 2, height / 7);

        gc.restore();
    }

    public static void menuItems(GraphicsContext gc, ArrayList<MenuItem> items, double time) {

        double animation = time / 500000000;
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();


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

            gc.setFont(UIDrawer.OPTION_TEXT);
            gc.setTextAlign(TextAlignment.CENTER);
            double x = width / 2;
            double y = (i + 3) * height / (items.size() + 3);
            gc.strokeText(item.getName(), x, y);
            gc.fillText(item.getName(), x, y);

            item.setHitbox(x - 140, y - 35,  280, 47);

            gc.restore();

        }

        gc.restore();

    }

}
