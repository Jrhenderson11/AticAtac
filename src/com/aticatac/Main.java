package com.aticatac;

import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.utils.SystemSettings;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {

        SystemSettings.setScreenHeight(480);
        SystemSettings.setScreenWidth(720);

        Main.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("AticAtac");
        primaryStage.setScene(new MainMenu(new Group(), primaryStage));
        primaryStage.show();


    }
}
