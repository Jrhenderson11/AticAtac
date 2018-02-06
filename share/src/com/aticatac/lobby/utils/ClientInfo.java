package com.aticatac.lobby.utils;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class ClientInfo  implements Serializable {

    private final String ip;
    private final String username;
    private boolean ready;
    private int color;

    public ClientInfo(String ip, String username, boolean ready, int color) {
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