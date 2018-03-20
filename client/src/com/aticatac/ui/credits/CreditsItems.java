package com.aticatac.ui.credits;

import com.aticatac.sound.SoundManager;
import com.aticatac.ui.mainmenu.utils.MenuItem;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CreditsItems {
    private String name;
    private boolean selected;
    private Supplier<Scene> link;
    private Rectangle hitbox;

    public CreditsItems(String name) {
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

    public Scene choose() {
        SoundManager m = new SoundManager();
        //m.stopMenuBg();
        //m.playClick();
        return link.get();
    }


    public void setHitbox(double x, double y, double w, double h) {
        this.hitbox = new Rectangle(x, y, w, h);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
