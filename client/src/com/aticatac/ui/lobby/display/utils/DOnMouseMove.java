package com.aticatac.ui.lobby.display.utils;

import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.utils.Drawable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DOnMouseMove implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        for (Drawable d : Displayer.getDrawables()) {
        }
    }
}
