package com.aticatac.ui.lobby.display.utils;

import com.aticatac.ui.utils.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class BackButton extends Button{

    public BackButton(Rectangle hitbox) {
        super(hitbox, "Leave Lobby");
    }

    @Override
    public void click() {

    }

    @Override
    public void draw(GraphicsContext gc, long now) {

    }
}
