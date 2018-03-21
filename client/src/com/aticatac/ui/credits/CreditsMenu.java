package com.aticatac.ui.credits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.aticatac.utils.SystemSettings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class CreditsMenu extends Scene {
    public CreditsMenu(Group root, Scene mainMenu, Stage primaryStage){
        super(root);
        ArrayList<CreditsItems> names = new ArrayList<>();
        names.add(new CreditsItems("Tom Taylor"));
        names.add(new CreditsItems("James Henderson"));
        names.add(new CreditsItems("Bianca Comanescu"));
        names.add(new CreditsItems("Safira Brilianti"));
        names.add(new CreditsItems("Lucy Griffiths"));
        names.add(new CreditsItems("Dave Jones"));

        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);

        CreditsAnimation animation = new CreditsAnimation(canvas, names, System.nanoTime());
        animation.start();

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (code == KeyCode.ESCAPE) {
                    primaryStage.setScene(mainMenu);
                }
            }
        });

        Set<KeyCode> pressedKeys = new HashSet<>();
        this.setOnMouseMoved(new CreditsMouseMoved(names));

    }
}
