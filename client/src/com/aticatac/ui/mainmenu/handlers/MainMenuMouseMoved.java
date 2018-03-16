package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static com.aticatac.ui.mainmenu.utils.MenuItem.unselectAll;

public class MainMenuMouseMoved implements EventHandler<MouseEvent> {

	/**
	 * The MenuItems that can be 'selected' by hovering over it
	 */
    private final ArrayList<MenuItem> menuItems;

    /**
     * Creates a handler for mouse moves
     * @param menuItems The list of MenuItems that can be selected
     */
    public MainMenuMouseMoved(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
    
    /**
     * Handles mouse events
     */
    @Override
    public void handle(MouseEvent event) {

        int mousedId = mouseInHitbox(event.getX(), event.getY());

        unselectAll(menuItems);

        if (mousedId == -1) return;
        else {
            menuItems.get(mousedId).select();
        }
    }

    /**
     * Returns the index of the hitbox of the MenuItem the mouse if in
     * @param x The mouse X position
     * @param y The mouse Y position
     * @return The index of the hitbox of the selected MenuItem
     */
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
