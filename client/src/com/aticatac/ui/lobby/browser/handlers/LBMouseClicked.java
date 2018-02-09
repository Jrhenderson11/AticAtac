package com.aticatac.ui.lobby.browser.handlers;

import com.aticatac.ui.lobby.browser.Browser;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LBMouseClicked implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        Browser.join();
    }
}
