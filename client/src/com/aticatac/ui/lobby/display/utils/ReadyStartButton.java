package com.aticatac.ui.lobby.display.utils;

import com.aticatac.ui.utils.Button;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import static java.lang.StrictMath.sin;

public class ReadyStartButton extends Button {

    public ReadyStartButton(Rectangle hitbox, String text) {
        super(hitbox, text);
    }

    @Override
    public void click() {

    }

    @Override
    public void draw(GraphicsContext gc, long now) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        Color fill = Color.GREEN;
        if (this.isSelected()) {
            double animation = (double) now / 500000000;
            fill = Color.rgb(0, (int) (200 + 50 * sin(animation)),  0);
        }

        // Border
        gc.setFill(fill);
        gc.setStroke(Color.BLACK);
        Rectangle hit = this.getHitbox();
        gc.fillRect(hit.getX(), hit.getY(), hit.getWidth(), hit.getHeight());
        gc.strokeRect(hit.getX(), hit.getY(), hit.getWidth(), hit.getHeight());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(UIDrawer.LOBBY_DISPLAY_TEXT);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.GRAY);

        gc.strokeText(this.getText(), 0.95 * width, 0.955 * height);
        gc.fillText(this.getText(),0.95 * width, 0.955 * height);

    }
}
