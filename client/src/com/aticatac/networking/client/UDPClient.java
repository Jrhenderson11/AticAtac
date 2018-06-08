package com.aticatac.networking.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyInfo;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.networking.globals.Globals;
import com.aticatac.world.World;

import javafx.concurrent.Task;
import javafx.scene.paint.Color;

public class UDPClient extends Task implements LobbyServer {

	private String name;
	private InetAddress address;
	private BlockingQueue<String> messageQueue;
	private ClientReceiver receiver;
	private ClientSender sender;
	private int status;
	private Lobby lobby;
	private LobbyInfo lobbyInfo = null;
	private ClientInfo myInfo;
	private boolean ready;

	private InetAddress myip = null;
	
	/**
	 *  makes a new UDPClient object
	 * @param newName this client's name
	 * @param newAddress server address to send messages to
	 */
	public UDPClient(String newName, InetAddress newAddress) {
		this.name = newName;
		this.address = newAddress;
		this.messageQueue = new LinkedBlockingQueue<String>();
		this.status = Globals.IN_LIMBO;
		//this.lobbyInfo = new LobbyInfo(0, 0, 0, "LOBBY NOT INITIALISED");
		this.ready = false;
	}

	/**
	 * javafx version of Thread.run()
	 */
	@Override
	public Object call() {

		receiver = new ClientReceiver(name, this);
		sender = new ClientSender(name, address, messageQueue);

		Thread sendThread = new Thread(sender);
		sendThread.setDaemon(true);
		sendThread.start();
		receiver.start();

		try {
			receiver.join();
		} catch (Exception e) {
		}

		// System.out.println(name + " stopped");
		return new Object();
	}

	/** sends message to server
	 * 
	 * @param data to send
	 */
	public void sendData(String data) {
		try {
			this.messageQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * attempts to stop threads
	 */
	public void halt() {
		sender.halt();
		sender.cancel();
		receiver.halt();
		// receiver.cancel();
	}

	/**
	 * @return most up to date model
	 */
	public World getModel() {
		return this.receiver.getModel();
	}

	/**
	 * Returns this client's status (see Globals for various statuses)
	 * @return The status of the client
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * Set the status of the client
	 * @param newStatus The integer that represents the client's new status (see Globals for various statuses)
	 */
	public void setStatus(int newStatus) {
		this.status = newStatus;
	}

	/**
	 * Get the lobby that the client is in
	 * @return This client's lobby 
	 */
	public Lobby getLobby() {
		return this.lobby;
	}

	/**
	 * Sets the lobby
	 * @param newLobby The lobby to set the client as being in
	 */
	public void setLobby(Lobby newLobby) {
		this.lobby = newLobby;
		this.myInfo = lobby.getClientBySocket(this.address, this.sender.getPort());
	}

	/**
	 * 
	 * @return this lobbyInfo
	 */
	public LobbyInfo getLobbyInfo() {
		return this.lobbyInfo;
	}
	
	public void setLobbyInfo(LobbyInfo newInfo) {
		this.lobbyInfo = newInfo;
		// System.out.println(this.lobbyInfo.MAX_PLAYERS);
		// System.out.println(newInfo.MAX_PLAYERS);
	}

	/* =========== */
	// Current interface stuff

	public void connect() {
		sendData("join");
	}

	public boolean makeLobby() {
		if (this.status == Globals.IN_LIMBO) {
			sendData("makelobby");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean joinLobby(int id, String password) {
		if (this.status == Globals.IN_LIMBO) {
			sendData("init");
			this.status = Globals.IN_LOBBY;
			System.out.println("IN LOBBY");
			return true;
		}
		return false;
	}

	public void addAIPlayer() {
		System.out.println("sending addAI");
		this.sendData("addAI");
	}
	
	@Override
	public boolean startGame() {
		System.out.println("I AM STARTING GAME!!!!");
		
		if (this.status == Globals.IN_LOBBY) {
			sendData("start");
			return true;
		}
		return false;
	}

	@Override
	public void readyUp() {
		this.ready = true;
		this.sendData("ready");
	}

	@Override
	public void unready() {
		this.ready = false;
		this.sendData("unready");
	}

	@Override
	public boolean leaveLobby() {
		if (this.status == Globals.IN_LOBBY) {
			this.sendData("leavelobby");
			this.status = Globals.IN_LIMBO;
			return true;
		}
		return false;
	}

	public void requestLobby() {
		//System.out.println("requesting lobby");
		this.sendData("lobbypls");
	}
	
	@Override
	public ArrayList<LobbyInfo> getPublicLobbies() {
		ArrayList<LobbyInfo> lobbies = new ArrayList<LobbyInfo>();
		if (this.lobbyInfo != null) {
			lobbies.add(this.lobbyInfo);		
		}	
		return lobbies;
	}

	@Override
	public Lobby updateLobby(int id) {
        return this.lobby;
	}

	@Override
	public boolean changeColor(Color color) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ClientInfo myInfo() {
		
		if (myip==null) {
			try {
				for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				    for (InetAddress inetAddress : Collections.list(netint.getInetAddresses())) {
				    	//System.out.println("InetAddress: " +  inetAddress);
				        //System.out.println("Address:" + address.toString().replaceAll("localhost", ""));
				        if (inetAddress.toString().substring(0, 3).equals(this.address.toString().replaceAll("localhost", "").substring(0, 3))) {
				        	//System.out.println("I've decided my ip is " + inetAddress);
				        	myip=inetAddress;
				        }
				    }
				}
				//System.out.println("cant find my ip");
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
    	return this.lobby.getClientBySocket(myip, this.sender.getPort());
		
		
	}

	public boolean leaveGame() {
		if (this.status == Globals.IN_GAME) {
			this.sendData("leavegame");
			this.status = Globals.IN_LIMBO;
			return true;
		}
		return false;
	}
	
	@Override
	public void kickClient(String id) {
		this.sendData("kick:" + id);
	}
	
	public void whatismyip() {
		this.sendData("whatismyip");
	}
	
	public void setMyIP(InetAddress newIP) {
		System.out.println("my ip set as " + newIP);
		this.myip = newIP;
	}

	public void quit() {
		this.sendData("quit");
	}
}