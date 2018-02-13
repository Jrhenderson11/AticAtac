package com.aticatac.lobby;

import java.io.Serializable;
import java.net.InetAddress;

//TODO: ClientInfo, Lobby, LobbyInfo all need to be shared by the server project @James

import java.util.ArrayList;

public class Lobby implements Serializable {

	// Min = 1, Max = 4
	private int max_players;

	private boolean game_started;

	private ClientInfo lobbyLeader;

	private ArrayList<ClientInfo> serfs;
	public final int ID;
	public final String NAME;

	public Lobby(ClientInfo lobbyLeader, LobbyInfo info) {
		this.lobbyLeader = lobbyLeader;
		serfs = new ArrayList<>();
		this.game_started = false;
		this.ID = info.ID;
		this.NAME = info.NAME;
	}
	
	public ClientInfo getLobbyLeader() {
		return lobbyLeader;
	}

	public ArrayList<ClientInfo> getPeasants() {
		return serfs;
	}

	public ArrayList<ClientInfo> getAll() {
		ArrayList<ClientInfo> all = new ArrayList<ClientInfo>(serfs);
		all.add(lobbyLeader);
		return all;
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

	public void setStarted() {
		this.game_started = true;
		System.out.println("started");
	}

	public boolean getStarted() {
		return this.game_started;
	}

	public ClientInfo getClientByID(String id) {
		for (ClientInfo i : this.getAll()) {
			if (i.getID().equals(id)) {
				return i;
			}
		}
		return null;
	}

	public ClientInfo getClientBySocket(InetAddress origin, int sourcePort) {
		for (ClientInfo i : this.getAll()) {
			if (i.getAddress().toString().replaceAll(" ", "").replaceAll("\n", "")
					.equals(origin.toString().replaceAll("localhost", "").replaceAll(" ", "").replaceAll("\n", ""))
					&& i.getOriginPort() == sourcePort) {
				return i;
			}
		}
		return null;
	}
}
