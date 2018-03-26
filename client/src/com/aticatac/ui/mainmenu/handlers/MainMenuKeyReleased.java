package com.aticatac.ui.mainmenu.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Set;

public class MainMenuKeyReleased implements EventHandler<KeyEvent> {

	/**
	 * Set of pressed keys
	 */
    private final Set<KeyCode> pressedKeys;

    /**
     * Creates a handler for keys on release.
     * @param pressedKeys The keys that have been pressed
     */
    public MainMenuKeyReleased(Set<KeyCode> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    /**
     * Handles the release of keys
     */
    @Override
    public void handle(KeyEvent event) {
        pressedKeys.remove(event.getCode());

    }
}
