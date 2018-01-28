package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.MenuItem;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.aticatac.ui.mainmenu.MenuItem.whichSelected;

public class MainMenuMouseClicked implements EventHandler<MouseEvent> {

    private final ArrayList<MenuItem> menuItems;
    private final Stage stage;

    public MainMenuMouseClicked(ArrayList<MenuItem> menuItems, Stage primaryStage) {
        this.menuItems = menuItems;
        this.stage = primaryStage;
    }

    @Override
    public void handle(MouseEvent event) {

        int selectedId = whichSelected(menuItems);

        if (selectedId == -1) return;
        else {
            stage.setScene(menuItems.get(selectedId).choose());
        }

    }
}
