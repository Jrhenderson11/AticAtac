package com.aticatac.ui;

import com.aticatac.utils.SystemSettings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;

public class MainMenu extends Scene{

    public MainMenu(Group root) {
        super(root);
        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.CYAN);
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Terminal", 48));
        gc.fillText("AticAtac", width / 2, height / 2);
    }


}
