package com.aticatac.ui.lobby.browser.utils;

import com.aticatac.lobby.LobbyInfo;
import com.aticatac.ui.lobby.browser.LobbyBrowser;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class LBDrawer {
    public static void lobbies(GraphicsContext gc, ArrayList<LobbyInfo> lobbies, int offset, long now) {

        gc.save();

        for (int i = offset; i < lobbies.size(); i++) {
            drawLobbyInfo(gc, i, lobbies.get(i), now);
        }

        gc.restore();

    }

    private static void drawLobbyInfo(GraphicsContext gc, int i, LobbyInfo lob, long now) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        boolean selected = i == LobbyBrowser.getSelected();

        // Name, ID, Current players
        gc.save();

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.gray(0.1));

        int x = width / 9;
        int y = i * height / 7;
        int w = 7 * width / 9;
        int h = height / 7;

        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);

        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(UIDrawer.LOBBYBROWSTEXT);
        gc.setStroke(Color.GRAY);
        gc.setFill(Color.WHITE);

        gc.strokeText("Name: " + lob.NAME, x + width / 100, y + height / 17);
        gc.fillText("Name: " + lob.NAME, x + width / 100, y + height / 17);

        gc.strokeText("ID: " + lob.ID, x + width / 100, y + 2 * height / 17);
        gc.fillText("ID: " + lob.ID, x + width / 100, y + 2 * height / 17);

        String players = "Players: " + lob.CURRENT_PLAYERS + " / " + lob.MAX_PLAYERS;
        gc.strokeText(players, x + 3 * width / 9, y + height / 13);
        gc.fillText(players, x + 3 * width / 9, y + height / 13);

        if (selected) {
            gc.setFill(Color.LIGHTGREEN);
        } else {
            gc.setFill(Color.GREEN);
        }

        gc.setStroke(Color.BLACK);
        gc.fillRect(x + 6 * width / 10, y + height / 30, width / 9, height / 12);
        gc.strokeRect(x + 6 * width / 10, y + height / 30, width / 9, height / 12);


        gc.setFont(UIDrawer.LOBBYBROWSTEXT);
        gc.setFill(Color.BLACK);
        gc.fillText("JOIN", x + width * 0.635, y + height * 0.085);

        gc.restore();

    }

    public static void create(GraphicsContext gc, long now) {
    }

    public static void back(GraphicsContext gc, long now) {
    }

    public static void borders(GraphicsContext gc) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        gc.setStroke(Color.BLACK);
        gc.strokeRect(width / 9, 0, 7 * width / 9, height);

    }
}
