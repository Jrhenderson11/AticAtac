package com.aticatac.networking.server;

import java.util.concurrent.CopyOnWriteArrayList;

import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.LobbyData;
import com.aticatac.networking.model.Model;

public class UDPServer extends Thread{

	
	
	private boolean running;
	private byte[] buffer = new byte[256];
	private CopyOnWriteArrayList<ClientInfo> clientList;
	private ServerReciever receiver;
	private ServerSender sender;
	private Model model;
	private LobbyData lobby;
	
	
	private int status;
	
	public UDPServer() {
	
		this.clientList = new CopyOnWriteArrayList<ClientInfo>();
		this.status = Globals.IN_GAME;
	}

	public void run() {
		
		this.model = new Model(0, 0);
		this.receiver = new ServerReciever(model, clientList, this); 
		this.sender = new ServerSender(model, clientList, this, lobby);
		receiver.start();
		sender.start();
		System.out.println("thread started and waiting to join");
		try {
			receiver.join();
			sender.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("Server stopped");
		
	}
	
	public void halt() {
		this.receiver.halt();
		this.sender.halt();
	}

	public int getStatus() {
		return this.status;
	}

	public void init() {
		model.init();
		this.status = Globals.IN_GAME;
	}
	
}
