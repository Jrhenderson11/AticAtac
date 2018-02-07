package com.aticatac.networking.server;

import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aticatac.lobby.utils.Lobby;
import com.aticatac.lobby.utils.LobbyInfo;
import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

import javafx.scene.paint.Color;

import com.aticatac.lobby.utils.ClientInfo;

public class UDPServer extends Thread{
	
	private boolean running;
	private byte[] buffer = new byte[256];
	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private ServerReciever receiver;
	private ServerSender sender;
	private Model model;
	private Lobby lobby;
	private LobbyInfo lobbyInfo;
	
	private int status;
	
	public UDPServer() {
	
		this.clientList = new CopyOnWriteArrayList<ConnectionInfo>();
		this.status = Globals.IN_LIMBO;
		this.lobbyInfo = new LobbyInfo(4, 0, 1);
	}

	public void run() {
		
		this.model = new Model(0, 0);
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

	public void startLobby(ClientInfo newClient) {
		model.init();
		this.lobby = new Lobby(newClient);
		this.status = Globals.IN_LOBBY;
	}
	
	public void joinLobby(String name, InetAddress address, int colour) {
		ClientInfo newClient = new ClientInfo(address.toString(), name, false, colour);
		if (this.status == Globals.IN_LIMBO) {
			//no lobby started so start one
			this.startLobby(newClient);
		} else if (this.status == Globals.IN_LOBBY) {
			this.lobby.addClient(newClient);
		}
		this.lobbyInfo = new LobbyInfo(4, 1 + this.lobby.getPeasants().size(), 1);
	}
	
	public Lobby getLobby() {
		return this.lobby;
	}
	
	public LobbyInfo getLobbyInfo() {
		return this.lobbyInfo;
	}
	
	public void startGame() {
		this.lobby.setStarted();
		System.out.println(this.lobby.getStarted());
		this.sender.sendAllLobby();
		//sleep to ensure clients are actually sent a lobby object
		this.status = Globals.IN_GAME;
	}
	
}
