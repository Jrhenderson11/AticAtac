package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.Lobby;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class LobbyHeader implements Drawable{
    private Lobby me;

    public LobbyHeader(Lobby lobby) {
        this.me = lobby;
    }

    @Override
    public void draw(GraphicsContext gc, long now) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(UIDrawer.LOBBY_DISPLAY_TEXT);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.strokeText(me.NAME,width * 0.075, height * 0.05);
        gc.fillText(me.NAME,width * 0.075, height * 0.05);

    }
}
