package com.aticatac.ui.mainmenu;

import com.aticatac.ui.mainmenu.handlers.MainMenuAnimationHandler;
import com.aticatac.ui.mainmenu.handlers.MainMenuKeyPressedHandler;
import com.aticatac.ui.mainmenu.handlers.MainMenuKeyReleasedHandler;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainMenu extends Scene{

    private final ArrayList<MenuItem> menuItems;
    private final Set<KeyCode> pressedKeys;

    public MainMenu(Group root) {
        super(root);

        this.menuItems = new ArrayList<>();
        this.menuItems.add(new MenuItem("Find a Lobby", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Create a Lobby", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Tutorial", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Settings", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Statistics", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Credits", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Quit", new Placeholder(new Group())));

        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        new MainMenuAnimationHandler(gc, menuItems, System.nanoTime()).start();

        this.pressedKeys = new HashSet<>();
        this.setOnKeyPressed(new MainMenuKeyPressedHandler(menuItems, pressedKeys));
        this.setOnKeyReleased(new MainMenuKeyReleasedHandler(pressedKeys));

        /*

        Not going to deal with resizing just yet

        this.heightProperty().addListener((observable, oldValue, newValue) ->
        {
            SystemSettings.setScreenHeight((int) ceil((Double) newValue));
            canvas.setHeight((Double) newValue);
        });
        this.widthProperty().addListener((observable, oldValue, newValue) ->
        {
            SystemSettings.setScreenWidth((int) ceil((Double) newValue));
            canvas.setWidth((Double) newValue);
        });

        */

    }


}
