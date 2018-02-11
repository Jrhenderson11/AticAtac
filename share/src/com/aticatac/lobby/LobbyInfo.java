package com.aticatac.lobby;

import java.io.Serializable;

public class LobbyInfo implements Serializable{
	public final String NAME;
	public final int MAX_PLAYERS;
    public final int CURRENT_PLAYERS;
    public final int ID;
    
    public LobbyInfo(int max_players, int current_players, int id, String name) {
        this.MAX_PLAYERS = max_players;
        this.CURRENT_PLAYERS = current_players;
        this.ID = id;
        this.NAME  = name;
    }
}