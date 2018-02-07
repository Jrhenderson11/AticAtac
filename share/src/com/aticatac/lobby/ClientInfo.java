package com.aticatac.lobby;

import javafx.scene.paint.Color;

public class ClientInfo {

    private final String id;
    private final String username;
    private boolean ready;
    private Color color;

    public ClientInfo(String id, String username, boolean ready, Color color) {
        this.id = id;
        this.username = username;
        this.ready = ready;
        this.color = color;
    }

    public void ready() {
        ready = true;
    }

    public void unready() {
        ready = false;
    }

    // TODO: maybe add connection strength and might want to store socket instead of id

}
