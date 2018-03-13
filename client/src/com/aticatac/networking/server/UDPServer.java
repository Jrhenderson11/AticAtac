package com.aticatac.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyInfo;
import com.aticatac.networking.globals.Globals;
import com.aticatac.utils.Controller;
import com.aticatac.world.Level;
import com.aticatac.world.Player;
import com.aticatac.world.World;

public class UDPServer extends Thread {

	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private ServerReciever receiver;
	private ServerSender sender;
	private World model;
	private Lobby lobby;
	private LobbyInfo lobbyInfo;

	private int status;

	public UDPServer() {
		this.clientList = new CopyOnWriteArrayList<ConnectionInfo>();
		this.status = Globals.IN_LIMBO;
		this.lobbyInfo = new LobbyInfo(4, 0, 1, "lobby1");
	}

	@Override
	public void run() {
		Level level = new Level(100, 100);
		level.randomiseMap();
		this.model = new World(level);
		this.receiver = new ServerReciever(model, clientList, this);
		this.sender = new ServerSender(model, clientList, this);
		receiver.start();
		sender.start();
		System.out.println("thread started and waiting to join");
		try {
			receiver.join();
			sender.join();
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("TestServer stopped");
	}

	public void halt() {
		this.receiver.halt();
		this.sender.halt();
	}

	public int getStatus() {
		return this.status;
	}

	public void startLobby(ClientInfo newClient, LobbyInfo info) {
		this.lobby = new Lobby(newClient, info);
		this.status = Globals.IN_LOBBY;
		System.out.println("new lobby created");
	}

	public void sendAllLobby() {
		this.sender.sendAllLobby();
	}

	public void joinLobby(String name, InetAddress address, int colour, int destPort, int originPort) {
		ClientInfo newClient;
		// create new lobby
		if (this.lobby == null) {
			newClient = new ClientInfo(name, false, 2, address, destPort, originPort);
		} else {
			newClient = new ClientInfo(name, false, this.lobby.getNextColour(), address, destPort, originPort);
		}

		if (this.status == Globals.IN_LIMBO) {
			// no lobby started so start one
			this.startLobby(newClient, this.lobbyInfo);
		} else if (this.status == Globals.IN_LOBBY) {
			System.out.println("adding " + newClient.getID() + " to lobby");
			this.lobby.addClient(newClient);
		} else if (this.status == Globals.IN_GAME) {
			System.out.println("adding " + newClient.getID() + " to lobby");
			this.lobby.addClient(newClient);

			if (this.model.getNumPlayers() < 4) {
				Player newPlayer = new Player(Controller.REAL, newClient.getID(), newClient.getColour());
				this.model.addPlayer(newPlayer);
			}
		}
		this.lobbyInfo = new LobbyInfo(4, this.lobby.getAll().size(), this.lobbyInfo.ID, this.lobbyInfo.NAME);
		System.out.println("Client joined lobby");
	}

	public void addAI() {
		int numAI = this.lobby.getBots().size() + 1;

		if (!this.lobby.addAI("AI" + numAI, this.lobby.getNextColour())) {
			System.out.println("lobby is full, not adding AI");
		} else {
			System.out.println("server adding AI");
		}
	}

	public void leaveLobby(InetAddress address, int originPort) {

		ClientInfo newClient = this.getClientInfo(address, originPort);
		if (this.lobby != null) {
			if ((this.status == Globals.IN_LOBBY) && this.lobby.getAll().contains(newClient)) {
				this.lobby.removeClient(newClient.getID());
			}
			this.lobbyInfo = new LobbyInfo(4, this.lobby.getAll().size(), 1, this.lobbyInfo.NAME);
			if (this.lobby.getAll().size() == 0) {
				this.status = Globals.IN_LIMBO;
			}
		}
		System.out.println("Client left lobby");
	}
	
	public Lobby getLobby() {
		return this.lobby;
	}

	public LobbyInfo getLobbyInfo() {
		return this.lobbyInfo;
	}

	public ClientInfo getClientInfo(InetAddress address, int port) {
		try {
			for (ClientInfo info : this.lobby.getAll()) {
				if (info.getAddress().equals(address) && info.getOriginPort() == port) {
					return info;
				}
			}
		} catch (NullPointerException e) {
			System.out.println("client disconnected and info no longer exists");
		}
		System.out.println("invalid client address to search for");
		return null;
	}

	public void setClientReady(InetAddress origin, int originPort) {
		if (this.getClientInfo(origin, originPort) == null) {
			return;
		}
		this.getClientInfo(origin, originPort).ready();
	}

	public void setClientUnReady(InetAddress origin, int originPort) {
		if (this.getClientInfo(origin, originPort) == null) {
			return;
		}
		this.getClientInfo(origin, originPort).unready();
	}

	public void startGame() {
		if (!this.lobby.allReady()) {
			return;
		}
		// give world lobby
		model.init(this.lobby);
		if (this.status == Globals.IN_LOBBY) {
			System.out.println("Game started");
			this.lobby.setStarted();
			for (int i = 0; i < 5; i++) {
				this.sender.sendAllLobby();
			}
			// sleep to ensure clients are actually sent a lobby object
			this.status = Globals.IN_GAME;
		} else {
			this.sender.sendAllLobby();
		}
	}

	public void replyIP(InetAddress origin, int originPort) {
		String msg = "IP:" + (origin.toString());
		this.sender.sendClientMessage(origin, originPort, msg);
	}

	public void removePlayer(InetAddress origin, int originPort) {
		if (this.status == Globals.IN_GAME) {
			this.model.removePlayer(this.getClientInfo(origin, originPort).getID());
		}
	}

	public void removeConnection(InetAddress origin, int originPort) {
		for (int i = 0; i < this.clientList.size(); i++) {
			if (this.clientList.get(i).getAddress().equals(origin)
					&& this.clientList.get(i).getOriginPort() == originPort) {
				this.clientList.remove(i);
			}
		}
		// reset state if only connection
		if (this.clientList.size() == 0) {
			this.status = Globals.IN_LIMBO;
		}

	}
}
