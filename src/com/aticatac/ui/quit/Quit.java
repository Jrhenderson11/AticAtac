package com.aticatac.ui.quit;

import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.ui.quit.handlers.QuitAnimation;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Quit extends Scene {

    private final Scene mainMenu;

    public Quit(Group root, Scene mainMenu) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        //TODO: Are you sure?

        this.mainMenu = mainMenu;

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer animation = new QuitAnimation(gc, System.nanoTime());

    }
}
