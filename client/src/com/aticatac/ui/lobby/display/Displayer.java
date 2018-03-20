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

    /**
     * Creates lobby object which displays info
     * @param root Root object
     * @param selected selected lobby id
     * @param server Server object
     * @param newParent Browser object
     * @param newStage Stage object
     */
    public Displayer(Group root, int selected, LobbyServer server, Browser newParent, Stage newStage) {
        super(root);

        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        DAnimator animator = new DAnimator(gc, selected, server, newParent, newStage);
        this.setOnMouseMoved(new DOnMouseMove());
        this.setOnMouseClicked(new DOnMouseClick());
        animator.start();
    }

    /**
     * @return List of drawable browser objects
     */
    public static HashSet<Drawable> getDrawables() {
        return drawables;
    }

    /**
     * Set drawable object
     * @param drawables List of Drawables
     */
    public static void setDrawables(HashSet<Drawable> drawables) {
        Displayer.drawables = drawables;
    }

    /**
     * @return Buttons
     */
    public static HashSet<Button> getButtons() {
        return buttons;
    }

    /**
     * @param buttons set button objects
     */
    public static void setButtons(HashSet<Button> buttons) {
        Displayer.buttons = buttons;
    }

    /**
     * Determine whether or not an object is moused on
     * @param box hitbox
     * @param x mouse x
     * @param y mouse y
     * @return mouses boolean
     */
    public static boolean moused(Rectangle box, double x, double y) {

        return x >= box.getX() && x <= box.getWidth() + box.getX()
                && y >= box.getY() && y <= box.getY() + box.getHeight();

    }

}
