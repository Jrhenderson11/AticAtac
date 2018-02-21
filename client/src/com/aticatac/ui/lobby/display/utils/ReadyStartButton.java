package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.utils.Button;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class ReadyStartButton extends Button {

	private LobbyServer server;
	private boolean ready = false;
	private final boolean isLeader;

	public ReadyStartButton(Rectangle hitbox, String text, LobbyServer newServer) {
		super(hitbox, text);
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

		if (this.isLeader) {
			server.startGame();
		} else {
			if (this.ready) {
				this.server.readyUp();
				this.ready = true;
			} else {
				this.server.unready();
				this.ready = false;
			}
		}
	}

	@Override
	public void draw(GraphicsContext gc, long now) {

	}
}
