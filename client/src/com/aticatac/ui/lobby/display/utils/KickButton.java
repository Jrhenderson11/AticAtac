package com.aticatac.ui.lobby.display.utils;

import com.aticatac.ui.utils.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class KickButton extends Button {

    public KickButton(Rectangle hitbox) {
        super(hitbox, "Kick");
    }

    @Override
    public void click() {

    }

    @Override
    public void draw(GraphicsContext gc, long now) {

    }
}
