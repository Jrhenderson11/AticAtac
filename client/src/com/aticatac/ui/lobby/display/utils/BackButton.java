package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.utils.Button;
import com.aticatac.utils.SystemSettings;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BackButton extends Button{

	private LobbyServer server;
	
    public BackButton(Rectangle hitbox, LobbyServer newServer) {
        super(hitbox, "Leave Lobby");
        this.server = newServer;
    }

    @Override
    public void click() {
    	this.server.leaveLobby();
    }

    @Override
    public void draw(GraphicsContext gc, long now) {
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);

        gc.fillRect(0, 0.8 * height, width * 0.1, height * 0.1);
        gc.strokeRect(0, 0.8 * height, width * 0.1, height * 0.1);

    }
}
