package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static com.aticatac.ui.mainmenu.utils.MenuItem.unselectAll;

public class MainMenuMouseMoved implements EventHandler<MouseEvent> {

    private final ArrayList<MenuItem> menuItems;

    public MainMenuMouseMoved(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public void handle(MouseEvent event) {

        int mousedId = mouseInHitbox(event.getX(), event.getY());

        unselectAll(menuItems);

        if (mousedId == -1) return;
        else {
            menuItems.get(mousedId).select();
        }
    }

    private int mouseInHitbox(double x, double y) {

        for (int i = 0; i < menuItems.size(); i++) {
            Rectangle box = menuItems.get(i).getHitbox();
            if (x >= box.getX() && x <= box.getWidth() + box.getX()
                    && y >= box.getY() && y <= box.getY() + box.getHeight()) {
                return i;
            }
        }
        return -1;
    }
}
