package com.aticatac.ui.lobby.browser.handlers;

import java.util.ArrayList;

import com.aticatac.lobby.LobbyServer;

import com.aticatac.lobby.utils.LobbyInfo;
import com.aticatac.ui.lobby.browser.Browser;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LBKeyPressed implements EventHandler<KeyEvent> {
    private final Scene back;
    private final LobbyServer server;
    private final Stage stage;

    public LBKeyPressed(Scene back, LobbyServer server, Stage primaryStage) {
        this.back = back;
        this.server = server;
        this.stage = primaryStage;
    }

    @Override
    public void handle(KeyEvent event) {

        int selected = Browser.getSelected();
        ArrayList<LobbyInfo> lobbyInfo = server.getPublicLobbies();

        switch (event.getCode()) {
            case DOWN:

                if (selected == -1) {
                    Browser.select(0);
                } else if (selected == server.getPublicLobbies().size() - 1) {
                    Browser.select(0);
                } else {
                    Browser.select(++selected);
                }

                break;

            case UP:

                if (selected == -1) {
                    Browser.select(lobbyInfo.size() - 1);
                } else if (selected == 0) {
                    Browser.select(lobbyInfo.size() - 1);
                } else {
                    Browser.select(--selected);
                }

                break;

            case ENTER:

                Browser.join();
                break;

            case BACK_SPACE:

                stage.setScene(back);
                break;

            default:
                break;
        }


    }
}
