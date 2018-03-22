package com.aticatac.ui.lobby.creater;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.mainmenu.MainMenu;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Creator extends Scene {

    public Creator(Group root, UDPClient server, MainMenu mainMenu, Stage primaryStage) {
        super(root);
        server.makeLobby();
      //  primaryStage.setScene(new Browser(new Group(), server, mainMenu, primaryStage));
        System.out.println("done");
    }
}
