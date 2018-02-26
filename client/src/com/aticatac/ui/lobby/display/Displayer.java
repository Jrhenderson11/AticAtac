package com.aticatac.ui.lobby.display;

import java.util.HashSet;

import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.lobby.display.handlers.DAnimator;
import com.aticatac.ui.lobby.display.handlers.DOnMouseClick;
import com.aticatac.ui.lobby.display.handlers.DOnMouseMove;
import com.aticatac.ui.utils.Button;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.utils.SystemSettings;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Displayer extends Scene {

    private static HashSet<Drawable> drawables;
    private static HashSet<Button> buttons;
    private static Browser parent;
    private static Stage stage;
    
    public Displayer(Group root, int selected, LobbyServer server, Browser newParent, Stage newStage) {
        super(root);

        this.parent = newParent;
        this.stage = newStage;
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        DAnimator animator = new DAnimator(gc, selected, server, parent, stage);
        this.setOnMouseMoved(new DOnMouseMove());
        this.setOnMouseClicked(new DOnMouseClick());
        animator.start();
    }

    public static HashSet<Drawable> getDrawables() {
        return drawables;
    }

    public static void setDrawables(HashSet<Drawable> drawables) {
        Displayer.drawables = drawables;
    }

    public static HashSet<Button> getButtons() {
        return buttons;
    }

    public static void setButtons(HashSet<Button> buttons) {
        Displayer.buttons = buttons;
    }

    public static boolean moused(Rectangle box, double x, double y) {

        if (x >= box.getX() && x <= box.getWidth() + box.getX()
                && y >= box.getY() && y <= box.getY() + box.getHeight()) {
            return true;
        }

        return false;
    }

}
