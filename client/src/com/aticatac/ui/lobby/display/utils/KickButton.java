package com.aticatac.ui.lobby.display.utils;

import com.aticatac.ui.utils.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class KickButton extends Button {

    public KickButton(int offset) {
        super(new Rectangle(), "Kick");
    }

    @Override
    public void click() {

    }

    @Override
    public void draw(GraphicsContext gc, long now) {

        gc.setFill(Color.ORANGE);
        gc.setStroke(Color.BLACK);
        Rectangle box = this.getHitbox();
        gc.strokeRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
        gc.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Kick", box.getX() + 0.5 * box.getWidth(), box.getY() + 0.5 * box.getHeight());
        gc.fillText("Kick", box.getX() + 0.5 * box.getWidth(), box.getY() + 0.5 * box.getHeight());

    }
}
