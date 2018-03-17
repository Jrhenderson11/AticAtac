package com.aticatac.ui.quit.utils;

import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.StrictMath.sin;

public class QuitDrawer {

    public static void options(GraphicsContext gc, ArrayList<Option> options, double animation) {

        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();

        for (int i = 0; i < options.size(); i++) {

            gc.save();

            gc.setStroke(Color.GRAY);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(UIDrawer.OPTION_TEXT);

            if (options.get(i).selected()) {
                gc.translate(2 * sin(animation),sin(animation));
                gc.setFill(Color.color(abs(sin(2 * animation)), abs(sin(3 * animation)), abs(sin(5 * animation))));
            } else {
                gc.setFill(Color.WHITE);
            }

            int x = 2 *( i + 1) * width / 5;
            int y = 2 * height / 3;
            options.get(i).setHitbox(new Rectangle(x - width / 10, y - height / 10, width / 8, height / 8));
            gc.fillText(options.get(i).getText(), x, y);
            gc.strokeText(options.get(i).getText(), x, y);
            gc.restore();

        }


    }

    public static void title(GraphicsContext gc, double animation) {

        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();

        gc.save();

        gc.setStroke(Color.WHITE);
        gc.setFill(Color.gray(abs(sin(animation))));

        gc.setFont(UIDrawer.TITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Are you sure?", width / 2, height / 3);
        gc.fillText("Are you sure?", width / 2, height / 3);

        gc.restore();

    }
}
