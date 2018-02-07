package com.aticatac.ui.lobby.browser.utils;

import com.aticatac.lobby.LobbyInfo;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class LBDrawer {
    public static void lobbys(GraphicsContext gc, ArrayList<LobbyInfo> lobbies) {

        gc.save();

        for (int i = 0; i < lobbies.size(); i++) {
            drawlobbyinfo(gc, i, lobbies.get(i));
        }

        gc.restore();

    }

    private static void drawlobbyinfo(GraphicsContext gc, int i, LobbyInfo lob) {




    }

    public static void join(GraphicsContext gc) {
    }

    public static void create(GraphicsContext gc) {
    }

    public static void back(GraphicsContext gc) {
    }
}
