package com.aticatac.lobby;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LobbyInfo implements Serializable{
	public final String NAME;
	public final int MAX_PLAYERS;
    public final int CURRENT_PLAYERS;
    public final int ID;
    
    /**
     * A container for information about a lobby
     * @param max_players The maximum number of players in a lobby
     * @param current_players The number of current players in the lobby
     * @param id The identifier of the lobby
     * @param name The name of the lobby
     */
    public LobbyInfo(int max_players, int current_players, int id, String name) {
        this.MAX_PLAYERS = max_players;
        this.CURRENT_PLAYERS = current_players;
        this.ID = id;
        this.NAME  = name;
    }
}