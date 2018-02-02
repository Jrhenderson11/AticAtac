package com.aticatac.ui.lobbymenu.testserver;

import com.aticatac.ui.lobbymenu.utils.Lobby;
import com.aticatac.ui.lobbymenu.utils.LobbyInfo;
import com.aticatac.ui.lobbymenu.utils.LobbyServer;

import java.util.ArrayList;

public class Server implements LobbyServer {

    @Override
    public Lobby joinLobby(int id, String password) {
        return null;
    }

    @Override
    public void readyUp() {

    }

    @Override
    public boolean startGame() {
        return false;
    }

    @Override
    public boolean leaveLobby() {
        return false;
    }

    @Override
    public ArrayList<LobbyInfo> getPublicLobbies() {
        return null;
    }
}
