package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.mainmenu.utils.MainMenuDrawer;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainMenuAnimation extends AnimationTimer {

    private final Canvas canvas;
    private final long then;
    private final ArrayList<MenuItem> menuItems;

    public MainMenuAnimation(Canvas canvas, ArrayList<MenuItem> menuItems, long then) {
        super();
        this.canvas = canvas;
        this.then = then;
        this.menuItems = menuItems;
    }

    @Override
    public void handle(long now) {
    	canvas.setWidth(SystemSettings.getScreenWidth());
    	canvas.setHeight(SystemSettings.getScreenHeight());
        UIDrawer.background(canvas.getGraphicsContext2D(), Color.gray(0.3));
        MainMenuDrawer.title(canvas.getGraphicsContext2D(), now - then);
        MainMenuDrawer.menuItems(canvas.getGraphicsContext2D(), menuItems, now - then);

    }

}
