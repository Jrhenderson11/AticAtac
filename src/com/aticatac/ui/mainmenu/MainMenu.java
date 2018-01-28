package com.aticatac.ui.mainmenu;

import com.aticatac.utils.SystemSettings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MainMenu extends Scene{

    public MainMenu(Group root) {
        super(root);
        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        new MainMenuHandler(gc, System.nanoTime()).start();

        /*

        Not going to deal with resizing just yet

        this.heightProperty().addListener((observable, oldValue, newValue) ->
        {
            SystemSettings.setScreenHeight((int) ceil((Double) newValue));
            canvas.setHeight((Double) newValue);
        });
        this.widthProperty().addListener((observable, oldValue, newValue) ->
        {
            SystemSettings.setScreenWidth((int) ceil((Double) newValue));
            canvas.setWidth((Double) newValue);
        });

        */

    }


}
