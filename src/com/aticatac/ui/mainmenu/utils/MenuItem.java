package com.aticatac.ui.mainmenu.utils;

import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MenuItem {
    private String name;
    private boolean selected;
    private Scene link;
    private Rectangle hitbox;

    public MenuItem(String name, Scene link) {
       this.name = name;
       this.link = link;
       this.selected = false;
       this.hitbox = new Rectangle( 0, 0, 0, 0);
    }

    public static int whichSelected(ArrayList<MenuItem> menuItems) {

        for (int i = 0; i < menuItems.size(); i++) {

            if(menuItems.get(i).selected()) return i;
        }

        return -1;

    }

    public static void unselectAll(ArrayList<MenuItem> menuItems) {
        for (MenuItem m : menuItems) {
            m.unselect();
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

    public Scene choose() { return link; }

    public void setHitbox(double x, double y, double w, double h) {
        this.hitbox = new Rectangle(x, y, w, h);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
