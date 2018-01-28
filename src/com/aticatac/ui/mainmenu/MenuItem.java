package com.aticatac.ui.mainmenu;

import javafx.scene.Scene;

public class MenuItem {
    private String name;
    private boolean selected;
    private Scene link;

    public MenuItem(String name, Scene link) {
       this.name = name;
       this.link = link;
       this.selected = false;
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
}
