package com.aticatac.ui.lobby.browser;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.handlers.LBAnimation;
import com.aticatac.ui.lobby.browser.handlers.LBKeyPressed;
import com.aticatac.ui.lobby.browser.handlers.LBMouseClicked;
import com.aticatac.ui.lobby.browser.handlers.LBMouseMoved;
import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.utils.SystemSettings;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Browser extends Scene {

    private static LobbyServer server;
    private static Stage stage = null;
    private static int selected;
    private static int offset;
    private static MainMenu mainMenu;
    
    public Browser(Group root, LobbyServer server, MainMenu back, Stage primaryStage, Boolean makeNew) {
        super(root);
        
        if (makeNew) {
        	server.makeLobby();
        }
        mainMenu = back;
        
        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);

        Browser.stage = primaryStage;
        Browser.server = server;

        selected = -1;
        offset = 0;

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        this.setOnKeyPressed(new LBKeyPressed(back, server, primaryStage, this));
        this.setOnMouseMoved(new LBMouseMoved());
        this.setOnMouseClicked(new LBMouseClicked(this));

        // TODO: add click listeners and key movement
        AnimationTimer animation = new LBAnimation(gc, server);
        animation.start();
        System.out.println("started");
    }

    public static void select(int i) {
        selected = i;
    }

    public static int getSelected() {
        return selected;
    }

    public static void setOffset(int offset) {
        Browser.offset = offset;
    }

    public static int getOffset() {
        return offset;
    }

    public void join() {
        if (selected == -1) return;
        if (server.joinLobby(selected, "")) {
        	Browser b = new Browser(new Group(), server, this.mainMenu, stage, false);
        	stage.setScene(new Displayer(new Group(), selected, server, b, stage, mainMenu));
        } else {
            System.err.println("Server rejected join");
        }
    }
}
