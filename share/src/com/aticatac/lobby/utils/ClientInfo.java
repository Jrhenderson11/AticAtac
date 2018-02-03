package com.aticatac.lobby.utils;

import javafx.scene.paint.Color;

public class ClientInfo {

    private final String ip;
    private final String username;
    private boolean ready;
    private Color color;

    public ClientInfo(String ip, String username, boolean ready, Color color) {
        this.ip = ip;
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

    // TODO: maybe add connection strength and might want to store socket instead of ip

}
