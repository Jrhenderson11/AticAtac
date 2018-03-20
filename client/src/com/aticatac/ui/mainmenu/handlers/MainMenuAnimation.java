package com.aticatac.ui.mainmenu.handlers;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.mainmenu.utils.MainMenuDrawer;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import com.aticatac.world.Level;
import com.aticatac.world.World;
import com.aticatac.world.utils.LevelGen;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainMenuAnimation extends AnimationTimer {

    private final Canvas canvas;
    private final long then;
    private final ArrayList<MenuItem> menuItems;
    private final Renderer render;

    /**
     * Creates a new MainMenuAnimation for the given MenuItems
     * @param canvas The Canvas to draw to
     * @param menuItems The list of MenuItems to animate
     * @param then The time as a long of the animation when initialised
     */
    public MainMenuAnimation(Canvas canvas, ArrayList<MenuItem> menuItems, long then) {
        super();
        this.canvas = canvas;
        this.then = then;
        this.menuItems = menuItems;
        this.render = new Renderer();
        render.setWorld(new World(new Level(LevelGen.get(100, 100))));
    }

    /**
     * The method called repeatedly by the super AnimationTimer
     * @param now The current time
     */
    @Override
    public void handle(long now) {
    	canvas.setWidth(SystemSettings.getScreenWidth());
    	canvas.setHeight(SystemSettings.getScreenHeight());


        render.render(canvas.getGraphicsContext2D());

        Color color = Color.BLACK;
        double opacity = 0.7;
        Color opaqueColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        UIDrawer.background(canvas.getGraphicsContext2D(), opaqueColor);


        MainMenuDrawer.title(canvas.getGraphicsContext2D(), now - then);
        MainMenuDrawer.menuItems(canvas.getGraphicsContext2D(), menuItems, now - then);

    }

}
