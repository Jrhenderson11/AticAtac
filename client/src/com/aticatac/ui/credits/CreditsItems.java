package com.aticatac.ui.credits;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;


public class CreditsItems {
    private String name;
    private boolean selected;
    private Rectangle hitbox;

    public CreditsItems(String name) {
        this.name = name;
        this.selected = false;
        this.hitbox = new Rectangle( 0, 0, 0, 0);
    }

    public static int whichSelected(ArrayList<CreditsItems> creditsItems) {

        for (int i = 0; i < creditsItems.size(); i++) {

            if(creditsItems.get(i).selected()) return i;
        }

        return -1;

    }

    public static void unselectAll(ArrayList<CreditsItems> creditsItems) {
        for (CreditsItems c : creditsItems) {
            c.unselect();
        }
    }

    public void select() {
        selected = true;
    }

    public void unselect() {
        selected = false;
    }

    public boolean selected() {
        return selected;
    }

    public String getName() {
        return name;
    }

    public void setHitbox(double x, double y, double w, double h) {
        this.hitbox = new Rectangle(x, y, w, h);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
