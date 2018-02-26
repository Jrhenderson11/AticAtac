package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.utils.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class KickButton extends Button {

	private LobbyServer server;
	private final boolean isLeader;
	private String id;
	private int lastclicked=1000;
	
    public KickButton(int offset, LobbyServer newServer, String newId) {
        super(new Rectangle(), "Kick");
        this.server = newServer;
        // TODO: replace placeholder 0
        if (server.myInfo().equals(server.updateLobby(0).getLobbyLeader())) {
          this.isLeader = true;
        } else {
          this.isLeader = false;
        }
        this.id = newId;
    }

    @Override
    public void click() {
    	if (lastclicked>1000) {
    		this.server.kickClient(id);
    		this.lastclicked=0;
    	}
    }

    @Override
    public void draw(GraphicsContext gc, long now) {
    	lastclicked++;
        gc.setFill(Color.ORANGE);
        gc.setStroke(Color.BLACK);
        Rectangle box = this.getHitbox();
        gc.strokeRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
        gc.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Kick", box.getX() + 0.5 * box.getWidth(), box.getY() + 0.5 * box.getHeight());
        gc.fillText("Kick", box.getX() + 0.5 * box.getWidth(), box.getY() + 0.5 * box.getHeight());

    }
}
