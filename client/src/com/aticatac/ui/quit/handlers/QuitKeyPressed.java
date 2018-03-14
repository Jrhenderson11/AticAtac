package com.aticatac.ui.quit.handlers;

import com.aticatac.ui.quit.utils.Option;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Set;

import static com.aticatac.ui.quit.utils.Option.whichSelected;


public class QuitKeyPressed implements EventHandler<KeyEvent> {

	/**
	 * The list of options that can be selected
	 */
    private final ArrayList<Option> options;

    /**
     * Creates a handler for when a key is pressed in the quit menu
     * @param options The list of options that can be selected
     */
    public QuitKeyPressed(ArrayList<Option> options) {
        this.options = options;
    }

    /**
     * Handles the key eventss
     */
    @Override
    public void handle(KeyEvent event) {
        KeyCode pressed = event.getCode();
        int selectedId = whichSelected(options);

        switch (pressed) {
            case RIGHT:
                if (selectedId == -1) {

                    options.get(0).select();

                } else if (selectedId == 0) {

                    options.get(selectedId).unselect();
                    options.get(1).select();

                } else {
                    options.get(selectedId).unselect();
                    options.get(0).select();
                }
                break;
            case LEFT:
                if (selectedId == -1) {

                    options.get(options.size() - 1).select();

                } else if (selectedId == 0) {

                    options.get(0).unselect();
                    options.get(options.size() - 1).select();

                } else {
                    options.get(selectedId--).unselect();
                    options.get(selectedId).select();
                }
                break;
            case ENTER:
                options.get(selectedId).execute();

        }
    }
}
