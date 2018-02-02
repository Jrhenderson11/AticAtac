package com.aticatac.ui.mainmenu;

import com.aticatac.ui.mainmenu.handlers.*;
import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.quit.Quit;
import com.aticatac.ui.tutorial.Tutorial;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainMenu extends Scene{

    private final ArrayList<MenuItem> menuItems;
    private final Set<KeyCode> pressedKeys;

    public MainMenu(Group root, Stage primaryStage) {
        super(root);

        // TODO: replace Placeholder

        this.menuItems = new ArrayList<>();
        this.menuItems.add(new MenuItem("Find a Lobby", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Create a Lobby", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Tutorial", new Tutorial(new Group())));
        this.menuItems.add(new MenuItem("Settings", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Statistics", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Credits", new Placeholder(new Group())));
        this.menuItems.add(new MenuItem("Quit", new Quit(new Group(), this, primaryStage)));

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        MainMenuAnimation animation = new MainMenuAnimation(gc, menuItems, System.nanoTime());
        animation.start();

        this.pressedKeys = new HashSet<>();
        this.setOnKeyPressed(new MainMenuKeyPressed(menuItems, pressedKeys, primaryStage, animation));
        this.setOnKeyReleased(new MainMenuKeyReleased(pressedKeys));
        this.setOnMouseMoved(new MainMenuMouseMoved(menuItems));
        this.setOnMouseClicked(new MainMenuMouseClicked(menuItems, primaryStage, animation));

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
