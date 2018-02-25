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

public class ReadyStartButton extends Button {

	private LobbyServer server;
	private boolean ready = false;
	private final boolean isLeader;

	private Stage stage;

	public ReadyStartButton(Rectangle hitbox, String text, LobbyServer newServer, Stage newStage) {
		super(hitbox, text);
		this.stage = newStage;
		this.server = newServer;
		// TODO: replace placeholder 0
		while (server.myInfo() == null)
			;
		if (server.myInfo().getID().equals(server.updateLobby(0).getLobbyLeader().getID())) {
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
			if (!server.myInfo().isReady()) {
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

		int width = SystemSettings.getNativeWidth();
		int height = SystemSettings.getNativeHeight();

		//TODO: replace placeholder 0
		Color fill = Color.GREEN;
		if (isLeader && !server.updateLobby(0).allReady()) {
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

		gc.strokeText(this.getText(), 0.95 * width, 0.955 * height);
		gc.fillText(this.getText(), 0.95 * width, 0.955 * height);

	}
}
