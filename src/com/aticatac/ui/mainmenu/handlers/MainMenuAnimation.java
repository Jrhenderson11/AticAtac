package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.MenuItem;
import com.aticatac.ui.utils.Drawer;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainMenuAnimation extends AnimationTimer {

    private final GraphicsContext gc;
    private final long then;
    private final ArrayList<MenuItem> menuItems;

    public MainMenuAnimation(GraphicsContext gc, ArrayList<MenuItem> menuItems, long then) {
        super();
        this.gc = gc;
        this.then = then;
        this.menuItems = menuItems;
    }

    @Override
    public void handle(long now) {

        Drawer.background(gc, Color.GRAY);
        Drawer.title(gc, now - then);
        Drawer.menuItems(gc, menuItems, now - then);

    }

}
