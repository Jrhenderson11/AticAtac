package com.aticatac.lobby.utils;

import java.io.Serializable;

public class LobbyInfo implements Serializable{
    public final int MAX_PLAYERS;
    public final int CURRENT_PLAYERS;
    public final int ID;

    public LobbyInfo(int max_players, int current_players, int id) {
        MAX_PLAYERS = max_players;
        CURRENT_PLAYERS = current_players;
        ID = id;
    }
}