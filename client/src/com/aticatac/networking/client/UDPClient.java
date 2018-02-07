package com.aticatac.networking.client;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.aticatac.lobby.utils.Lobby;
import com.aticatac.lobby.utils.LobbyInfo;
import com.aticatac.lobby.utils.LobbyServer;
import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

import javafx.concurrent.Task;

public class UDPClient extends Task implements LobbyServer{

	private String name;
	private InetAddress address;
	private byte[] buffer;
	private BlockingQueue<String> messageQueue;
	private ClientReceiverThread receiver;
	private ClientSender sender;
	private int status;
	private Lobby lobby;
	private LobbyInfo lobbyInfo;

	public UDPClient(String newName, InetAddress newAddress) {
		this.name = newName;
		this.address = newAddress;
		this.messageQueue = new LinkedBlockingQueue<String>();
		this.status = Globals.IN_LIMBO;
		this.lobbyInfo = new LobbyInfo(0, 0, 0);
	}

	@Override
	public Object call() {

		receiver = new ClientReceiverThread(name, this);
		sender = new ClientSender(name, address, messageQueue);

		Thread sendThread = new Thread(sender);
		sendThread.setDaemon(true);
		sendThread.start();
		receiver.start();

		try {
			receiver.join();
		} catch (Exception e) {
		}
		// sender.cancel();
		// receiver.cancel();

		// System.out.println(name + " stopped");
		return new Object();
	}

	public void sendData(String data) {
		try {
			this.messageQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void halt() {
		sender.halt();
		sender.cancel();
		receiver.halt();
		// receiver.cancel();
	}

	public Model getModel() {
		return this.receiver.getModel();
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int newStatus) {
		this.status = newStatus;
	}

	public Lobby getLobby() {
		return this.lobby;
	}

	public void setLobby(Lobby newLobby) {
		this.lobby = newLobby;
	}

	public LobbyInfo getLobbyInfo() {
		return this.lobbyInfo;
	}

	public void setLobbyInfo(LobbyInfo newInfo) {
		this.lobbyInfo = newInfo;
	}

	/* =========== */
	// Current interface stuff

	public void connect() {
		sendData("join");
	}
	
	@Override
	public Lobby joinLobby(int id, String password) {
		if (this.status == Globals.IN_LIMBO) {
			sendData("init");
			this.status = Globals.IN_LOBBY;
		}
		return null;
	}
	
	@Override
	public boolean startGame() {
		if (this.status == Globals.IN_LOBBY) {
			sendData("start");
			return true;
		}
		return false;
	}

	@Override
	public void readyUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unready() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean leaveLobby() {
		return false;
	}

	@Override
	public ArrayList<LobbyInfo> getPublicLobbies() {
		// TODO Auto-generated method stub
		return null;
	}
}