package com.aticatac.ui.lobbymenu;

import com.aticatac.lobby.utils.LobbyServer;
import com.aticatac.ui.lobbymenu.handlers.LMAnimation;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class LobbyBrowser extends Scene {

    public LobbyBrowser(Group root, LobbyServer server) {
        super(root);

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer animation = new LMAnimation(gc, server);


    }
}
