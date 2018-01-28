package com.aticatac.ui.mainmenu.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Set;

public class MainMenuKeyReleasedHandler implements EventHandler<KeyEvent> {

    private final Set<KeyCode> pressedKeys;

    public MainMenuKeyReleasedHandler(Set<KeyCode> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    @Override
    public void handle(KeyEvent event) {
        pressedKeys.remove(event.getCode());

    }
}
