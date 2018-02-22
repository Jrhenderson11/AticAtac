package com.aticatac.ui.lobby.display.utils;

import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.utils.Button;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class DOnMouseMove implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        for (Button b : Displayer.getButtons()) {
            b.setSelected(moused(b.getHitbox(), event.getX(), event.getY()));
        }
    }

    private boolean moused(Rectangle box, double x, double y) {

        if (x >= box.getX() && x <= box.getWidth() + box.getX()
                && y >= box.getY() && y <= box.getY() + box.getHeight()) {
            return true;
        }

        return false;
    }
}
