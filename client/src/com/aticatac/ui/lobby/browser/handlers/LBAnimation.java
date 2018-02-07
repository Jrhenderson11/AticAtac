package com.aticatac.ui.lobby.browser.handlers;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.LobbyInfo;
import com.aticatac.ui.lobby.browser.utils.LBDrawer;
import com.aticatac.ui.utils.UIDrawer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LBAnimation extends AnimationTimer {

    private final GraphicsContext gc;
    private final Scene back;
    private final ClientInfo me;
    private ArrayList<LobbyInfo> lobbies;

    public LBAnimation(GraphicsContext gc, ArrayList<LobbyInfo> lobbies, Scene back, ClientInfo clientInfo) {

        this.me = clientInfo;
        this.gc = gc;
        this.back = back;
        this.lobbies = lobbies;

    }

    @Override
    public void handle(long now) {

        UIDrawer.background(gc, Color.GREY);
        LBDrawer.lobbys(gc, lobbies);
        LBDrawer.join(gc);
        LBDrawer.create(gc);
        LBDrawer.back(gc);

    }
}
