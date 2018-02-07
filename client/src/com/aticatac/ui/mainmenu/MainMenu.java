package com.aticatac.ui.mainmenu;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.LobbyBrowser;
import com.aticatac.ui.lobby.testserver.TestServer;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainMenu extends Scene{

    public MainMenu(Group root, Stage primaryStage) {
        super(root);

        // TODO: replace Placeholder

        // TODO: get client info
        LobbyServer server = new TestServer(new ClientInfo("127.0.0.1", "Tom", false, Color.GREEN));

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Find a Lobby", new LobbyBrowser(new Group(), server, this, primaryStage)));
        menuItems.add(new MenuItem("Create a Lobby", new Placeholder(new Group())));
        menuItems.add(new MenuItem("Tutorial", new Tutorial(new Group())));
        menuItems.add(new MenuItem("Settings", new Placeholder(new Group())));
        menuItems.add(new MenuItem("Statistics", new Placeholder(new Group())));
        menuItems.add(new MenuItem("Credits", new Placeholder(new Group())));
        menuItems.add(new MenuItem("Quit", new Quit(new Group(), this, primaryStage)));

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        MainMenuAnimation animation = new MainMenuAnimation(gc, menuItems, System.nanoTime());
        animation.start();

        Set<KeyCode> pressedKeys = new HashSet<>();
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
