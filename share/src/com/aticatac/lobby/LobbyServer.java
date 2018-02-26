package com.aticatac.lobby;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public interface LobbyServer {
	
    // Only allowed when client is in a lobby
    Lobby updateLobby(int id);
    // Only give full testserver info once client actually joins
    boolean joinLobby(int id, String password);

    // rup or default
    void readyUp();

    // brb
    void unready();

    // can only be called if testserver leader && all players ready
    boolean startGame();

    // True if successful, false otherwise. Retry if false
    boolean leaveLobby();

    //kicks client with this id
    void kickClient(String id);
    
    boolean changeColor(Color color);

    int getStatus();

    void setStatus(int newStatus);
    
    ClientInfo myInfo();

    // Get list of all public lobbies
    ArrayList<LobbyInfo> getPublicLobbies();

    // TODO: change colour or map or some bullshit
}