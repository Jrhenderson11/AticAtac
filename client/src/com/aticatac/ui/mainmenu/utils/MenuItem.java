package com.aticatac.ui.mainmenu.utils;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.aticatac.Main;
import com.aticatac.sound.SoundManager;

import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

public class MenuItem {
	
	/**
	 * Name of this menu item
	 */

    private String name;
    /**
     * Whether this menu item is selected or not
     */
    private boolean selected;
    /**
     * The Scene to change to when this item is clicked
     */
    private Supplier<Scene> link;
    /**
     * The Hitbox of this menu item
     */
    private Rectangle hitbox;

    /**
     * Creates a new MenuItem with the given name and link to a new scene
     * @param name The name of this object
     * @param link The scene to switch to when this item is selected in the menu
     */
    public MenuItem(String name, Supplier<Scene> link) {
       this.name = name;
       this.link = link;
       this.selected = false;
       this.hitbox = new Rectangle( 0, 0, 0, 0);
    }

    /**
     * Returns the index of the MenuItem that is selected from the list of items
     * @param menuItems The list of MenuItems to check
     * @return The index of the MenuItem within the list that is selected
     */
    public static int whichSelected(ArrayList<MenuItem> menuItems) {
        for (int i = 0; i < menuItems.size(); i++) {

            if(menuItems.get(i).selected()) return i;
        }

        return -1;

    }

    /**
     * Unselects all MenuItems
     * @param menuItems The list of items in the Main Menu
     */
    public static void unselectAll(ArrayList<MenuItem> menuItems) {
        for (MenuItem m : menuItems) {
            m.unselect();
        }
    }

    /**
     * Set this MenuItem to be selected
     */
    public void select() {
        selected = true;
    }

    /**
     * Set this MenuItem to be unselecteds
     */
    public void unselect() {
        selected = false;
    }

    /**
     * Returns whether this MenuItem is selected or not
     * @return True if this item is selcted
     */
    public boolean selected() {
        return selected;
    }

    /**
     * Gets the name of this object
     * @return Returns the name of this object
     */
    public String getName() {
        return name;
    }

    /**
     * Choose this MenuItem, returns the 'link' Scene
     * @return Returns the Scene this menu item links to 
     */
    public Scene choose() { 
    	SoundManager m = new SoundManager(Main.soundEnabled);
    	m.stopMenuBg();
    	//m.playClick();
    	return link.get(); 
    }
    /**
     * Sets the hitbox of this MenuItem to the given dimension
     * @param x The x position
     * @param y The y position
     * @param w The width
     * @param h The height
     */
    public void setHitbox(double x, double y, double w, double h) {
        this.hitbox = new Rectangle(x, y, w, h);
    }

    /**
     * Get the hitbox of this item
     * @return The Rectangle defining the collision boundaries of the hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
}
