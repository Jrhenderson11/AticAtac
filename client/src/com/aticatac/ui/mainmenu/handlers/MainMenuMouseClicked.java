package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.aticatac.ui.mainmenu.utils.MenuItem.whichSelected;

public class MainMenuMouseClicked implements EventHandler<MouseEvent> {
	
	/**
	 * The list of MenuItems that can be clicked
	 */
    private final ArrayList<MenuItem> menuItems;
    /**
     * The Stage (window)
     */
    private final Stage stage;
    /**
     * The MainMenu animation
     */
    private final MainMenuAnimation animation;

    /**
     * Creates a handler for mouse clicks
     * @param menuItems The menu items in the Main Menu
     * @param primaryStage JavaFX stage
     * @param animation The animation for the Main Menu
     */
    public MainMenuMouseClicked(ArrayList<MenuItem> menuItems, Stage primaryStage, MainMenuAnimation animation) {
        this.menuItems = menuItems;
        this.stage = primaryStage;
        this.animation = animation;
    }

    
    /**
     * Handles the mouse event
     */
    @Override
    public void handle(MouseEvent event) {

        int selectedId = whichSelected(menuItems);

        if (selectedId == -1) return;
        else {
            stage.setScene(menuItems.get(selectedId).choose());
        }

    }
}
