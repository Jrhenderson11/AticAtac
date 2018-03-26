package com.aticatac.ui.quit.utils;

import javafx.scene.shape.Rectangle;
import java.util.ArrayList;


import java.util.function.Function;

public class Option {
	
	/**
	 * Whether this option is selected or not
	 */
    private boolean selected;
    /**
     * The function that happens on selection
     */
    private Function<Void, Void> select;
    /**
     * The display text for this option
     */
    private String text;
    /**
     * The hitbox for selecting this option
     */
    private Rectangle hitbox;

    /**
     * Creates a new option with the given function and text
     * @param select The function that happens on selection of this option 
     * @param text The display text of this option
     */
    public Option(Function<Void, Void> select, String text) {
        this.select = select;
        this.text = text;
        this.hitbox = new Rectangle(0, 0);
        this.selected = false;
    }
    
    /**
     * Returns the index of th option that is selected from a list of options
     * @param option The list of options to check are selected
     * @return The index of the first one found to be selected
     */
    public static int whichSelected(ArrayList<Option> option) {

        for (int i = 0; i < option.size(); i++) {

            if(option.get(i).selected()) return i;
        }

        return -1;

    }

    /**
     * Unselects all options within the list
     * @param option The list of options
     */
    public static void unselectAll(ArrayList<Option> option) {
        for (Option O : option) {
            O.unselect();
        }
    }
    
    /**
     * Returns whether this option is selected or now
     * @return True if this option is selected
     */
    public boolean selected() {
        return selected;
    }

    /**
     * Selects this option
     */
    public void select() {
        selected = true;
    }

    /**
     * Unselects this option
     */
    public void unselect() {
        selected = false;
    }

    /**
     * Executes the function for when this option is selected
     */
    public void execute() {
        select.apply(null);
    }

    /**
     * Returns the text of this option
     * @return Returns the text of this option
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the hitbox to the given rectangle
     * @param rectangle The rectangle defining the hit box collision boundaries
     */
    public void setHitbox(Rectangle rectangle) {
        this.hitbox = rectangle;
    }

    /**
     * Gets the hitbox of this Option
     * @return The rectangle defining the hit box collision boundaries
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
}
