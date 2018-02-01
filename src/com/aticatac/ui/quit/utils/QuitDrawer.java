package com.aticatac.ui.quit.utils;

import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.StrictMath.sin;

public class QuitDrawer {

    public static void options(GraphicsContext gc, ArrayList<Option> options, long animation) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        for (int i = 0; i < options.size(); i++) {

            gc.save();

            gc.setStroke(Color.GRAY);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(UIDrawer.OPTION_TEXT);

            if (options.get(i).selected()) {
                gc.translate(2 * sin(animation),sin(animation));
                gc.setFill(Color.color(sin(2 * animation), sin(3 * animation), sin(5 * animation)));
            } else {
                gc.setFill(Color.WHITE);
            }

            gc.fillText(options.get(i).getText(), 2 *( i + 1) * width / 6, 2 * height / 3);
            gc.strokeText(options.get(i).getText(), 2 * (i + 1) * width / 6, 2 * height / 3);

            gc.restore();

        }


    }

    public static void title(GraphicsContext gc, long animation) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

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
