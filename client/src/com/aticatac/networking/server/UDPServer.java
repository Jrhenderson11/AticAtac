package com.aticatac.networking.server;

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

import javafx.concurrent.Task;

public class UDPServer extends Task {

	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private ServerReciever receiver;
	private ServerSender sender;
	private World model;
	private Lobby lobby;
	private LobbyInfo lobbyInfo;

	private int status;

	/**
	 * Cosntructor... (sets up new client lsit and resets status)
	 */
	public UDPServer() {
		this.clientList = new CopyOnWriteArrayList<ConnectionInfo>();
		this.status = Globals.IN_LIMBO;
		this.lobbyInfo = null;
	}

	/**
	 * javafx version of Thread.run()
	 */
	@Override
	public Object call() {
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
		return null;
	}

	/**
	 * stops this running
	 */
	public void halt() {
		this.receiver.halt();
		this.sender.halt();
	}

	/**
	 * returns this servers status 
	 * @return status code
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 *  Uses a clientInfo obj to initialise a new lobby 
	 * @param newClient clients info to use
	 * @param info Lobby info to use
	 */
	public void startLobby(ClientInfo newClient, LobbyInfo info) {
		this.lobby = new Lobby(newClient, info);
		this.status = Globals.IN_LOBBY;
		System.out.println("new lobby created");
	}

	/**
	 * send every client a lobby object
	 */
	public void sendAllLobby() {
		this.sender.sendAllLobby();
	}

	/**
	 * create a new lobbyInfo
	 */
	public void makeLobby() {
		if (this.lobbyInfo == null) {
			this.lobbyInfo = new LobbyInfo(4, 0, 1, "Lobby");
		}
	}

	/**
	 * Add a client to a lobby
	 * @param name client name
	 * @param address client IP address
	 * @param colour client colour
	 * @param destPort client destination port
	 * @param originPort client source port
	 */
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
				Player newPlayer = new Player(Controller.REAL, newClient.getID(), newClient.getColour(), model);
				this.model.addPlayer(newPlayer);
			}
		}
		this.lobbyInfo = new LobbyInfo(4, this.lobby.getAll().size(), this.lobbyInfo.ID, this.lobbyInfo.NAME);
		System.out.println("Client joined lobby");
	}

	/**
	 * add an ai player to the lobby
	 */
	public void addAI() {
		int numAI = this.lobby.getBots().size() + 1;

		if (!this.lobby.addAI("AI" + numAI, this.lobby.getNextColour())) {
			System.out.println("lobby is full, not adding AI");
		} else {
			System.out.println("server adding AI");
		}
	}

	/**
	 * remove a player from the lobby
	 * @param address IP to remove
	 * @param originPort source port to remove
	 */
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

	/**
	 * gets this lobby
	 * @return the lobby object
	 */
	public Lobby getLobby() {
		return this.lobby;
	}

	/**
	 * gets this servers lobby information
	 * @return the LobbyInfo object
	 */
	public LobbyInfo getLobbyInfo() {
		return this.lobbyInfo;
	}

	/**
	 * gets a clientinfo object based on IP and port
	 * @param address IP to look for
	 * @param port port to look for
	 * @return ClientInfo obj
	 */
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

	/**
	 *  set a clients state to ready
	 * @param origin IP
	 * @param originPort source port
	 */
	public void setClientReady(InetAddress origin, int originPort) {
		if (this.getClientInfo(origin, originPort) == null) {
			return;
		}
		this.getClientInfo(origin, originPort).ready();
	}

	/**
	 * remove client state ready
	 * @param origin IP
	 * @param originPort source port
	 */
	public void setClientUnReady(InetAddress origin, int originPort) {
		if (this.getClientInfo(origin, originPort) == null) {
			return;
		}
		this.getClientInfo(origin, originPort).unready();
	}

	/**
	 * begins the game
	 */
	public void startGame() {
		System.out.println("STARTING GAME");
		if (!this.lobby.allReady()) {
			return;
		}
		// give world lobby
		model.init(this.lobby);
		model.newRound();
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

	/**
	 * debug message, tells clinet what its external IP is
	 * @param origin ip to reply to
	 * @param originPort client source port
	 */
	public void replyIP(InetAddress origin, int originPort) {
		String msg = "IP:" + (origin.toString());
		this.sender.sendClientMessage(origin, originPort, msg);
	}

	/**
	 * removes player from game
	 * @param origin IP
	 * @param originPort client source port
	 */
	public void removePlayer(InetAddress origin, int originPort) {
		if (this.status == Globals.IN_GAME) {
			this.model.removePlayer(this.getClientInfo(origin, originPort).getID());
		}
	}

	/**
	 * removes connection from connection list
	 * @param origin IP
	 * @param originPort source port
	 */
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
