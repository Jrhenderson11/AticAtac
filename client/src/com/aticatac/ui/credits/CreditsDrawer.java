package com.aticatac.ui.credits;

import com.aticatac.ui.utils.UIDrawer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CreditsDrawer {

    public static void title(GraphicsContext gc, long time) {
        gc.save();

        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        double animation = (double) time / 1000000000;

        Color col = Color.color(abs(sin(animation)), abs(cos(0.5 * animation)), abs(cos(0.3 * animation)));

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(col);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(UIDrawer.TITLE_FONT);
        gc.fillText("Credits", width / 2, height / 7);
        gc.strokeText("Credits", width / 2, height / 7);

        gc.restore();
    }
    public static void creditsItems(GraphicsContext gc, ArrayList<CreditsItems> items, double time) {

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

            CreditsItems item = items.get(i);

            if (item.selected()) {
                gc.setFill(sfill);
                gc.translate(0 , 1.5 * sin(animation));
            } else {
                gc.setFill(fill);
            }

            Color col = Color.color(abs(sin(animation)), abs(cos(0.5 * animation)), abs(cos(0.3 * animation)));
            gc.setFont(UIDrawer.OPTION_TEXT);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFill(col);
            gc.setStroke(Color.BLACK);
            gc.setFont(UIDrawer.TITLE_FONT);

            double x = width / 2;
            double y = (i + 3) * height / (items.size() + 3);
            gc.strokeText(item.getName(), x, y);
            gc.fillText(item.getName(), x, y);

            item.setHitbox(x - 100, y - 15,  250, 40);

            gc.restore();

        }

        gc.restore();

    }
}
