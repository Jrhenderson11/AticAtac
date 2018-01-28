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
}
