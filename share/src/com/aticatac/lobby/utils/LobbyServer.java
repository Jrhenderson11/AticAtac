package com.aticatac.lobby.utils;

import java.util.ArrayList;

public interface LobbyServer {
	
    // Only give full testserver info once client actually joins
    Lobby joinLobby(int id, String password);

    // rup or default
    void readyUp();

    // brb
    void unready();

    // can only be called if testserver leader && all players ready
    boolean startGame();

    // True if successful, false otherwise. Retry if false
    boolean leaveLobby();

    // Get list of all public lobbies
    ArrayList<LobbyInfo> getPublicLobbies();

    // TODO: change colour or map or some bullshit
}
