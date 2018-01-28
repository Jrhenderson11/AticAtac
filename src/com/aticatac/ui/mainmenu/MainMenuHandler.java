package com.aticatac.ui.mainmenu;

import com.aticatac.ui.utils.Drawer;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainMenuHandler extends AnimationTimer {

    private final GraphicsContext gc;
    private final long then;
    private final int width;
    private final int height;
    private ArrayList<MenuItem> menuItems;

    public MainMenuHandler(GraphicsContext gc, long then) {
        super();
        this.gc = gc;
        this.then = then;
        this.width = SystemSettings.getScreenWidth();
        this.height = SystemSettings.getScreenHeight();
        this.menuItems = new ArrayList<>();
        this.menuItems.add(new MenuItem("Lobby", new Placeholder(new Group())));
    }

    @Override
    public void handle(long now) {

        Drawer.background(gc, Color.GRAY);
        Drawer.title(gc, now - then);

    }

}
