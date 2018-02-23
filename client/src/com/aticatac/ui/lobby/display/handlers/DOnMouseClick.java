package com.aticatac.ui.lobby.display.handlers;

import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.utils.Button;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import static com.aticatac.ui.lobby.display.Displayer.moused;

public class DOnMouseClick implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {

        for (Button b : Displayer.getButtons()) {
            if(moused(b.getHitbox(), event.getX(), event.getY())) b.click();
        }

    }
}
