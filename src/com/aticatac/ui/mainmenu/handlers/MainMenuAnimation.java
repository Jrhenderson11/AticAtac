package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.mainmenu.utils.MainMenuDrawer;
import javafx.animation.AnimationTimer;
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

        MainMenuDrawer.background(gc, Color.GRAY);
        MainMenuDrawer.title(gc, now - then);
        MainMenuDrawer.menuItems(gc, menuItems, now - then);

    }

}
