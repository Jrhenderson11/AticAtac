package com.aticatac.ui.mainmenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.mainmenu.handlers.MainMenuAnimation;
import com.aticatac.ui.mainmenu.handlers.MainMenuKeyPressed;
import com.aticatac.ui.mainmenu.handlers.MainMenuKeyReleased;
import com.aticatac.ui.mainmenu.handlers.MainMenuMouseClicked;
import com.aticatac.ui.mainmenu.handlers.MainMenuMouseMoved;
import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.quit.Quit;
import com.aticatac.ui.tutorial.AiDemo;
import com.aticatac.ui.tutorial.Tutorial;
import com.aticatac.ui.tutorial.TutorialNetworked;
import com.aticatac.ui.tutorial.SinglePlayer;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainMenu extends Scene {

	public MainMenu(Group root, Stage primaryStage, UDPClient server) {
        super(root);
        
     //   Supplier something = (() -> new Placeholder(new Group()));
    
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Find a Lobby", () -> new Browser(new Group(), server, this, primaryStage)));
        menuItems.add(new MenuItem("Create a Lobby", () -> new Placeholder(new Group())));
        menuItems.add(new MenuItem("AI Demo", () -> new AiDemo(new Group())));
        menuItems.add(new MenuItem("Tutorial", () -> new Tutorial(new Group())));
        menuItems.add(new MenuItem("SinglePlayer Demo", () -> new SinglePlayer(new Group())));
        menuItems.add(new MenuItem("MultiPlayer Demo", () -> new TutorialNetworked(new Group(), server)));
        menuItems.add(new MenuItem("Settings", () -> new Placeholder(new Group())));
        menuItems.add(new MenuItem("Statistics", () -> new Placeholder(new Group())));
        menuItems.add(new MenuItem("Credits", () -> new Placeholder(new Group())));
        menuItems.add(new MenuItem("Quit", () -> new Quit(new Group(), this, primaryStage)));

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
        {
            canvas.setHeight((int) ((double) newValue));
        });
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
        {
        	canvas.setWidth((int) ((double) newValue));
        });
        
        MainMenuAnimation animation = new MainMenuAnimation(canvas, menuItems, System.nanoTime());
        animation.start();

        Set<KeyCode> pressedKeys = new HashSet<>();
        this.setOnKeyPressed(new MainMenuKeyPressed(menuItems, pressedKeys, primaryStage, animation));
        this.setOnKeyReleased(new MainMenuKeyReleased(pressedKeys));
        this.setOnMouseMoved(new MainMenuMouseMoved(menuItems));
        this.setOnMouseClicked(new MainMenuMouseClicked(menuItems, primaryStage, animation));

        

    }

}
