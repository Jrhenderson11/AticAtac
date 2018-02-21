package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.utils.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class KickButton extends Button {

	private LobbyServer server;
	private final boolean isLeader;

	
    public KickButton(int offset, LobbyServer newServer) {
        // TODO get hitbox from offset
        super(new Rectangle(), "Kick");
        this.server = newServer;
		// TODO: replace placeholder 0
		if (server.myInfo().equals(server.updateLobby(0).getLobbyLeader())) {
			this.isLeader = true;
		} else {
			this.isLeader = false;
		}
    }

    @Override
    public void click() {

    }

    @Override
    public void draw(GraphicsContext gc, long now) {

    }
}
