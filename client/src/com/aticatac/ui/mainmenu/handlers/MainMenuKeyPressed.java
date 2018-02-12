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

    private final ArrayList<MenuItem> menuItems;
    private final Stage stage;
    private final MainMenuAnimation animation;
    private Set<KeyCode> pressedKeys;

    public MainMenuKeyPressed(ArrayList<MenuItem> menuItems, Set<KeyCode> pressedKeys, Stage primaryStage, MainMenuAnimation animation) {
        this.menuItems = menuItems;
        this.pressedKeys = pressedKeys;
        this.stage = primaryStage;
        this.animation = animation;

    }

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

            animation.stop(); // TODO: Not sure if this is needed. Remove from mouse clicked if not
            stage.setScene(menuItems.get(whichSelected(menuItems)).choose());

        }

    }

}
