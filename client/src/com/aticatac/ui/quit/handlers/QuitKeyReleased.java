package com.aticatac.ui.quit.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Set;

public class QuitKeyReleased implements EventHandler<KeyEvent> {
    private final Set<KeyCode> pressedKeys;

    public QuitKeyReleased(Set<KeyCode> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    @Override
    public void handle(KeyEvent event) {
        pressedKeys.remove(event.getCode());

    }
}
