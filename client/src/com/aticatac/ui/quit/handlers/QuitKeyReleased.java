package com.aticatac.ui.quit.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Set;

public class QuitKeyReleased implements EventHandler<KeyEvent> {
	
	/**
	 * The set of keys pressed
	 */
    private final Set<KeyCode> pressedKeys;

    /**
     * Creates a handler for keys released in the quit menu
     * @param pressedKeys The keys that have been pressed
     */
    public QuitKeyReleased(Set<KeyCode> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    /**
     * Handles key events
     */
    @Override
    public void handle(KeyEvent event) {
        pressedKeys.remove(event.getCode());

    }
}
