package com.aticatac.lobby.utils;

import java.io.Serializable;

//TODO: ClientInfo, Lobby, LobbyInfo all need to be shared by the server project @James

import java.util.ArrayList;

public class Lobby implements Serializable{

	// Min = 1, Max = 4
	private int max_players;

	private ClientInfo lobbyLeader;

	private ArrayList<ClientInfo> serfs;

	public Lobby(ClientInfo lobbyLeader) {
		this.lobbyLeader = lobbyLeader;
		serfs = new ArrayList<>();
	}

	public ClientInfo getLobbyLeader() {
		return lobbyLeader;
	}

	public ArrayList<ClientInfo> getPeasants() {
		return serfs;
	}

	// Make sure not full
	public boolean addClient(ClientInfo client) {
		if (serfs.size() == 3) {
			return false;
		} else {
			serfs.add(client);
			return true;
		}
	}

	public void removeClient(ClientInfo client) {
		serfs.remove(client);
	}
}
