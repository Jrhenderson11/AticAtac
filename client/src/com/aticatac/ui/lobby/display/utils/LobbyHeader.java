package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.Lobby;
import com.aticatac.ui.utils.Drawable;
import javafx.scene.canvas.GraphicsContext;

public class LobbyHeader implements Drawable{
    private Lobby me;

    public LobbyHeader(Lobby lobby) {
        this.me = lobby;
    }

    @Override
    public void draw(GraphicsContext gc, long now) {

    }
}
