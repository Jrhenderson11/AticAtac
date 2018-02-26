package com.aticatac.ui.lobby.display.utils;

import static java.lang.StrictMath.sin;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.utils.Button;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class BackButton extends Button {

	private LobbyServer server;
	private Stage stage;
	private Browser parent;
	
    public BackButton(Rectangle hitbox, LobbyServer newServer,Browser newParent, Stage newStage) {
        super(hitbox, "Back");
        this.server = newServer;
        this.parent = newParent;
        this.stage = newStage;
    }

    @Override
    public void click() {
    	System.out.println("CLICK BACK");
    	this.server.leaveLobby();
    	this.stage.setScene(parent);
    }

    @Override
    public void draw(GraphicsContext gc, long now) {
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        Color fill = Color.RED;
        if (this.isSelected()) {
            double animation = (double) now / 500000000;
            fill = Color.rgb((int) (200 + 50 * sin(animation)), 0, 0);
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

        gc.strokeText(this.getText(), 0.05 * width, 0.955 * height);
        gc.fillText(this.getText(), 0.05 * width, 0.955 * height);

    }
}
