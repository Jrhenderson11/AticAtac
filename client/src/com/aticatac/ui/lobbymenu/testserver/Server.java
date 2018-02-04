package com.aticatac.ui.lobbymenu.testserver;

import java.util.ArrayList;

import com.aticatac.lobby.utils.Lobby;
import com.aticatac.lobby.utils.LobbyInfo;
import com.aticatac.lobby.utils.LobbyServer;

public class Server implements LobbyServer {

    //@Override
    public Lobby joinLobby(int id, String password) {
        return null;
    }

    //@Override
    public void readyUp() {

    }

    //@Override
    public boolean startGame() {
        return false;
    }

    //@Override
    public boolean leaveLobby() {
        return false;
    }

    //@Override
    public ArrayList<LobbyInfo> getPublicLobbies() {
        return null;
    }
}
