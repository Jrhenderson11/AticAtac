package com.aticatac.ui.lobby.browser.handlers;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.lobby.browser.utils.LBDrawer;
import com.aticatac.ui.utils.UIDrawer;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LBAnimation extends AnimationTimer {

    private final GraphicsContext gc;
    private final ClientInfo me;
    private final LobbyServer server;

    public LBAnimation(GraphicsContext gc, LobbyServer server, ClientInfo clientInfo) {

        this.me = clientInfo;
        this.gc = gc;
        this.server = server;

    }

    @Override
    public void handle(long now) {

        UIDrawer.background(gc, Color.GREY);
        LBDrawer.borders(gc);
        // TODO: handle scrolling and offset
        LBDrawer.lobbies(gc, server.getPublicLobbies(), Browser.getOffset(), now);
        LBDrawer.create(gc, now);
        LBDrawer.back(gc, now);

    }
}
