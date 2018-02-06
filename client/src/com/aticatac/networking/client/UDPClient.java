package com.aticatac.networking.client;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.aticatac.lobby.utils.Lobby;
import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

import javafx.concurrent.Task;

public class UDPClient extends Task {

	private String name;
	private InetAddress address;
	private byte[] buffer;
	private BlockingQueue<String> messageQueue;
	private ClientReceiverThread receiver;
	private ClientSender sender;
	private int status;
	private Lobby lobby;
	
	public UDPClient(String newName, InetAddress newAddress) {
		this.name = newName;
		this.address = newAddress;
		this.messageQueue = new LinkedBlockingQueue<String>();
		this.status = Globals.IN_GAME;
	}

	@Override
	public Object call() {

		receiver = new ClientReceiverThread(name, this);
		sender = new ClientSender(name, address, messageQueue);
		
		Thread sendThread = new Thread(sender);
		sendThread.setDaemon(true);
		sendThread.start();
		//Thread recThread = new Thread(receiver);
		//recThread.setDaemon(true);
		//recThread.start();
		receiver.start();
		
		try {
			receiver.join();
			
		} catch (Exception e) {
			
		}
		//sender.cancel();
		//receiver.cancel();
		
		//System.out.println(name + " stopped");
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
		//receiver.cancel();
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
}