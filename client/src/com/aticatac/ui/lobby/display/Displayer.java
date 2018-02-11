package com.aticatac.ui.lobby.display;

import com.aticatac.lobby.Lobby;
import javafx.scene.Group;
import javafx.scene.Scene;

public class Displayer extends Scene {

    private final Lobby lobby;

    public Displayer(Group root, Lobby lobby) {
        super(root);
        this.lobby = lobby;
    }
}
