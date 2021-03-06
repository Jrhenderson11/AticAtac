package com.aticatac.ui.quit.handlers;

import com.aticatac.ui.quit.utils.Option;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.aticatac.ui.quit.utils.Option.whichSelected;


public class QuitMouseClicked implements EventHandler<MouseEvent> {

    private final ArrayList<Option> options;
    private final Stage stage;
    private final AnimationTimer animation;

    /**
     * Creates a handler for mouse click events in the Quit menu
     * @param options The options that can be clicked
     * @param primaryStage The Stage (window)
     * @param animation The animation
     */
    public QuitMouseClicked(ArrayList<Option> options, Stage primaryStage, AnimationTimer animation) {
        this.options = options;
        this.stage = primaryStage;
        this.animation = animation;
    }

    /**
     * Handles mouse events
     */
    @Override
    public void handle(MouseEvent event) {

        int selectedId = whichSelected(options);

        if (selectedId == -1) return;
        else {
            options.get(selectedId).execute();
        }

    }
}
