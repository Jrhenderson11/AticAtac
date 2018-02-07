package com.aticatac.ui.lobby.browser;

import com.aticatac.lobby.LobbyInfo;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.handlers.LBAnimation;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class LobbyBrowser extends Scene {

    private int selected;

    public LobbyBrowser(Group root, LobbyServer server, Scene back) {
        super(root);

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        ArrayList<LobbyInfo> lobbies = new ArrayList<>();

        // TODO: add click listeners and key movement

        AnimationTimer animation = new LBAnimation(gc, lobbies , back, server.myInfo());
        animation.start();

    }

    public void select(int i) {
        selected = i;
    }

    public int getSelected() {
        return selected;
    }
}
