package com.aticatac.ui.lobby.browser;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.handlers.LBAnimation;
import com.aticatac.ui.lobby.browser.handlers.LBKeyPressed;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class LobbyBrowser extends Scene {

    private static int selected;

    public LobbyBrowser(Group root, LobbyServer server, Scene back, Stage primaryStage) {
        super(root);

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        selected = -1;

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        this.setOnKeyPressed(new LBKeyPressed(back, server, primaryStage));

        // TODO: add click listeners and key movement
        AnimationTimer animation = new LBAnimation(gc, server , back, server.myInfo());
        animation.start();

    }

    public static void select(int i) {
        selected = i;
    }

    public static int getSelected() {
        return selected;
    }
}
