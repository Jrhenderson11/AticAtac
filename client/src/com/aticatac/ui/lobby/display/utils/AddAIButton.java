package com.aticatac.ui.lobby.display.utils;

import static java.lang.StrictMath.sin;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.networking.client.UDPClient;
import com.aticatac.ui.tutorial.MultiPlayer;
import com.aticatac.ui.utils.Button;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AddAIButton extends Button {

	private LobbyServer server;
	private boolean ready = false;

	public AddAIButton(Rectangle hitbox, LobbyServer newServer) {
		super(hitbox, "add AI");
		this.server = newServer;
	}

	@Override
	public void click() {
		System.out.println("addAI Clicked");
		server.addAIPlayer();
	}

	@Override
	public void draw(GraphicsContext gc, long now) {

		int width = SystemSettings.getScreenWidth();
		int height = SystemSettings.getScreenHeight();

		//TODO: replace placeholder 0
		Color fill = Color.GREEN;
		//lobby is full
		if (server.updateLobby(0).lobbySize()==4) {
			fill=Color.GRAY;
		} else {
			if (this.isSelected()) {
				double animation = (double) now / 500000000;
				fill = Color.rgb(0, (int) (200 + 50 * sin(animation)), 0);
			}
		}

		// Border
		gc.setFill(fill);
		gc.setStroke(Color.BLACK);
		Rectangle hit = this.getHitbox();
		gc.fillRect(hit.getX(), hit.getY(), hit.getWidth(), hit.getHeight());
		gc.strokeRect(hit.getX(), hit.getY(), hit.getWidth(), hit.getHeight());

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(UIDrawer.LOBBY_DISPLAY_TEXT);
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.GRAY);

		gc.strokeText(this.getText(), 0.45 * width, 0.955 * height);
		gc.fillText(this.getText(), 0.45 * width, 0.955 * height);

	}
}
