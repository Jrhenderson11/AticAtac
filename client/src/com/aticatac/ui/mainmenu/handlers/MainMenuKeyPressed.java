package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Set;

import static com.aticatac.ui.mainmenu.utils.MenuItem.whichSelected;

public class MainMenuKeyPressed implements EventHandler<KeyEvent> {

	/**
	 * The list of MenuItems to cycle through with keypresses
	 */
    private final ArrayList<MenuItem> menuItems;
    /**
     * The Stage (window)
     */
    private final Stage stage;
    private final MainMenuAnimation animation;
    private Set<KeyCode> pressedKeys;

    /**
     * Creates a handler for key presses in the main meun
     * @param menuItems The list of MenuItems to cycle through
     * @param pressedKeys The Set of key(s) pressed
     * @param primaryStage The Stage (window) used by this handler
     * @param animation The MainMenuAnimation used by this handler
     */
    public MainMenuKeyPressed(ArrayList<MenuItem> menuItems, Set<KeyCode> pressedKeys, Stage primaryStage, MainMenuAnimation animation) {
        this.menuItems = menuItems;
        this.pressedKeys = pressedKeys;
        this.stage = primaryStage;
        this.animation = animation;
    }

    /**
     * Handles KeyEvents for the given event
     * @param The KeyEvent to handle
     */
    @Override
    public void handle(KeyEvent event) {

        if(!pressedKeys.contains(event.getCode())) {
            pressedKeys.add(event.getCode());
        }

        int selectedId = whichSelected(menuItems);

        if(pressedKeys.contains(KeyCode.DOWN)) {

            if (selectedId == -1) {

                menuItems.get(0).select();

            } else if (selectedId + 1 == menuItems.size()) {

                menuItems.get(selectedId).unselect();
                menuItems.get(0).select();

            } else {
                menuItems.get(selectedId++).unselect();
                menuItems.get(selectedId).select();
            }

        } else if (pressedKeys.contains(KeyCode.UP)) {

            if (selectedId == -1) {

                menuItems.get(menuItems.size() - 1).select();

            } else if (selectedId == 0) {

                menuItems.get(0).unselect();
                menuItems.get(menuItems.size() - 1).select();

            } else {
                menuItems.get(selectedId--).unselect();
                menuItems.get(selectedId).select();
            }
        } else if (pressedKeys.contains(KeyCode.ENTER)) {

            stage.setScene(menuItems.get(whichSelected(menuItems)).choose());

        }

    }

}
