package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.MenuItem;
import com.aticatac.utils.SystemSettings;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Set;

public class MainMenuKeyPressedHandler implements EventHandler<KeyEvent> {

    private final ArrayList<MenuItem> menuItems;
    private Set<KeyCode> pressedKeys;

    public MainMenuKeyPressedHandler(ArrayList<MenuItem> menuItems, Set<KeyCode> pressedKeys) {
        this.menuItems = menuItems;
        this.pressedKeys = pressedKeys;

    }

    @Override
    public void handle(KeyEvent event) {

        if(!pressedKeys.contains(event.getCode())) {
            pressedKeys.add(event.getCode());
        }

        int selectedId = whichSelected();

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
        }

    }

    private int whichSelected() {

        for (int i = 0; i < menuItems.size(); i++) {

            if(menuItems.get(i).selected()) return i;
        }

        return -1;

    }
}
