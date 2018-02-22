package com.aticatac.ui.lobby.display;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.display.handlers.DAnimator;
import com.aticatac.ui.lobby.display.utils.BackButton;
import com.aticatac.ui.lobby.display.utils.ClientInfoBrick;
import com.aticatac.ui.lobby.display.utils.DOnMouseMove;
import com.aticatac.ui.utils.Button;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.utils.SystemSettings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;

public class Displayer extends Scene {

    private static HashSet<Drawable> drawables;
    private static HashSet<Button> buttons;

    public Displayer(Group root, int selected, LobbyServer server) {
        super(root);

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        DAnimator animator = new DAnimator(gc, selected, server);
        this.setOnMouseMoved(new DOnMouseMove());
        animator.start();
    }

    public static HashSet<Drawable> getDrawables() {
        return drawables;
    }

    public static void setDrawables(HashSet<Drawable> drawables) {
        Displayer.drawables = drawables;
    }
}
