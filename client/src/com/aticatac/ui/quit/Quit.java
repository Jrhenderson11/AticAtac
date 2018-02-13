package com.aticatac.ui.quit;

import com.aticatac.ui.quit.handlers.*;
import com.aticatac.ui.quit.utils.Option;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Quit extends Scene {

    public Quit(Group root, Scene mainMenu, Stage primaryStage) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        ArrayList<Option> options = new ArrayList<>();

        Function<Void, Void> yes = nothing -> {
            System.exit(0);
            return null;
        };

        Function<Void, Void> no = nothing -> {
            primaryStage.setScene(mainMenu);
            return null;
        };

        options.add(new Option(yes, "Yes"));
        options.add(new Option(no, "No"));

        this.setOnKeyPressed(new QuitKeyPressed(options));

        AnimationTimer animation = new QuitAnimation(gc,options, System.nanoTime());

        animation.start();

        Set<KeyCode> pressedKeys = new HashSet<>();
        this.setOnKeyPressed(new QuitKeyPressed(options));
        this.setOnKeyReleased(new QuitKeyReleased(pressedKeys));
        this.setOnMouseMoved(new QuitMouseMoved(options));
        this.setOnMouseClicked(new QuitMouseClicked(options, primaryStage, animation));


    }
}
