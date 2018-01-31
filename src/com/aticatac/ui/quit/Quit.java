package com.aticatac.ui.quit;

import com.aticatac.ui.quit.handlers.QuitAnimation;
import com.aticatac.ui.quit.utils.Option;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.function.Function;

public class Quit extends Scene {

    private final Scene mainMenu;

    public Quit(Group root, Scene mainMenu, Stage primaryStage) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        //TODO: Are you sure?

        this.mainMenu = mainMenu;

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Function<Void, Void> yes = new Function<Void, Void>() {
            @Override
            public Void apply(Void aVoid) {
                System.exit(0);
                return null;
            }
        };

        Function<Void, Void> no = new Function<Void, Void>() {
            @Override
            public Void apply(Void aVoid) {
                primaryStage.setScene(mainMenu);
                return null;
            }
        };

        ArrayList<Option> options = new ArrayList<Option>();
        options.add(new Option(yes, "Yes"));
        options.add(new Option(no, "No"));

        AnimationTimer animation = new QuitAnimation(gc,options, System.nanoTime());

    }
}
