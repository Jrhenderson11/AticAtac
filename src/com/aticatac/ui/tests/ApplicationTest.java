package com.aticatac.ui.tests;

import com.aticatac.ui.MainMenu;
import com.aticatac.utils.SystemSettings;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationTest extends Application {
    private static Scene currentScene;
    private static Scene nextScene;
    private static boolean updateScene;

    public static void main(String[] args) {
        Application.launch(args);

        currentScene = new MainMenu(new Group());
        updateScene = false;

        SystemSettings.setScreenHeight(480);
        SystemSettings.setScreenHeight(720);
    }

    public static void setNextScene(Scene nextScene) {
        ApplicationTest.nextScene = nextScene;
        ApplicationTest.updateScene = true;
    }

    @Override
    public void start(Stage primaryStage) {

        if (updateScene) {
            currentScene = nextScene;
            updateScene = false;
        }

        primaryStage.setTitle("AticAtac");
        primaryStage.setScene(currentScene);
        primaryStage.show();

    }
}
