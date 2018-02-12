package com.aticatac.ui.lobby.browser;

import java.io.File;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.handlers.LBAnimation;
import com.aticatac.ui.lobby.browser.handlers.LBKeyPressed;
import com.aticatac.ui.lobby.browser.handlers.LBMouseClicked;
import com.aticatac.ui.lobby.browser.handlers.LBMouseMoved;
import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Browser extends Scene {

    private static LobbyServer server;
    private static Stage stage = null;
    private static int selected;
    private static int offset;

    public Browser(Group root, LobbyServer server, Scene back, Stage primaryStage) {
        super(root);
        /*
        String musicFile = "assets/click.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
       	*/

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        Browser.stage = primaryStage;
        Browser.server = server;

        selected = -1;
        offset = 0;

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        this.setOnKeyPressed(new LBKeyPressed(back, server, primaryStage));
        this.setOnMouseMoved(new LBMouseMoved());
        this.setOnMouseClicked(new LBMouseClicked());

        // TODO: add click listeners and key movement
        AnimationTimer animation = new LBAnimation(gc, server);
        animation.start();

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

    public static void join() {

        if (selected == -1) return;

        if (server.joinLobby(selected, "")) {
            stage.setScene(new Displayer(new Group(), selected, server));
        } else {
            System.err.println("Server rejected join");
        }
    }
}
